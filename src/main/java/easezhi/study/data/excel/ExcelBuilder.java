package easezhi.study.data.excel;

import easezhi.study.data.excel.annotation.ExcelColumn;
import easezhi.study.data.excel.annotation.ExcelEntity;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import java.io.OutputStream;
import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static easezhi.study.data.excel.ExcelUtil.getterName;

public class ExcelBuilder <E> {

    Workbook workbook;

    String[] titles;

    int titleRowIndex; // 标题所在行，从0开始
    int dataRowIndex; // 数据所在行
    boolean freezeTitle;

    String sheetName;

    ColumnSpec[] columnSpecs;

    String defaultDateFormat = "yyyy-MM-dd";
    String defaultDateTimeFormat = "yyyy-MM-dd HH:mm";

    Integer defaultColumnWidth;

    Integer rowHeight = 16; // 默认行高，单位像素。

    public ExcelBuilder<E> init(Class<E> clazz) throws NoSuchMethodException {
        workbook = new SXSSFWorkbook();

        var excelAnno = clazz.getAnnotation(ExcelEntity.class);
        sheetName = excelAnno.sheet();
        titles = excelAnno.title();
        if (excelAnno.dateFormat().length() > 0) defaultDateFormat = excelAnno.dateFormat();
        if (excelAnno.dateTimeFormat().length() > 0) defaultDateTimeFormat = excelAnno.dateTimeFormat();
        if (excelAnno.width() > 0) defaultColumnWidth = excelAnno.width();
        titleRowIndex = 0; // 如果有其他标题样式，再扩展
        dataRowIndex = 1;
        freezeTitle = excelAnno.freezeTitle();

        if (excelAnno.width() > 0) { // 默认列宽
            defaultColumnWidth = excelAnno.width();
        } else if (excelAnno.width() < 0) { // 擦除默认列宽
            defaultColumnWidth = null;
        }
        if (excelAnno.rowHeight() > 0) { // 默认行高
            rowHeight = excelAnno.rowHeight();
        } else if (excelAnno.rowHeight() < 0) {
            rowHeight = null;
        }

        columnSpecs = buildColumnSpecs(clazz, titles);
        return this;
    }

    public void build(OutputStream os, List<E> docList) throws Exception {
        var sheet = workbook.createSheet(sheetName);
        if (rowHeight != null) sheet.setDefaultRowHeightInPoints(rowHeight);

        createTitleRow(sheet);

        for (int r = 0; r < docList.size(); r++) {
            var doc = docList.get(r);
            var row = sheet.createRow(r + dataRowIndex);
            for (var spec: columnSpecs) {
                spec.createCell(row, doc);
            }
        }
        System.out.println(LocalDateTime.now());
        workbook.write(os);
    }

    void createTitleRow(Sheet sheet) {
        var titleFont = workbook.createFont(); // 标题样式
        titleFont.setFontName("黑体");
        var titleStyle = workbook.createCellStyle();
        titleStyle.setAlignment(HorizontalAlignment.CENTER);
        titleStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        titleStyle.setFont(titleFont);

        var titleRow = sheet.createRow(titleRowIndex);
        for (int i = 0; i < titles.length; i++) {
            var cell = titleRow.createCell(i);
            cell.setCellValue(titles[i]);
            cell.setCellStyle(titleStyle);

            var colSpec = columnSpecs[i];
            if (colSpec.width != null) {
                sheet.setColumnWidth(i, colSpec.width * 256 + 128); // 每一个单位是 1/256 个字符宽度，再加上 padding 宽度
            }
        }
        if (freezeTitle) {
            sheet.createFreezePane(0, titleRowIndex + 1);
        }
    }

    ColumnSpec[] buildColumnSpecs(Class clazz, String[] titles) throws NoSuchMethodException {
        Map<String, Field> fieldMap = ExcelUtil.getAnnotatedFields(clazz);
        ColumnSpec[] specs = new ColumnSpec[titles.length];
        for (int i = 0; i < titles.length; i++) {
            var title = titles[i];
            var field = fieldMap.get(title);
            var spec = buildColumnSpec(clazz, field);
            spec.colIndex = i;
            specs[i] = spec;
        }
        return specs;
    }

    ColumnSpec[] buildColumnSpecs(Class clazz) throws NoSuchMethodException {
        List<Field> fields = new ArrayList<>();
        for (var field: clazz.getDeclaredFields()) {
            var colAnno = field.getAnnotation(ExcelColumn.class);
            if (colAnno == null || colAnno.column().length() == 0) continue;
            fields.add(field);
        }

        ColumnSpec[] specs = new ColumnSpec[titles.length];
        for (int i = 0; i < fields.size(); i++) {
            var field = fields.get(i);
            var spec = buildColumnSpec(clazz, field);
            specs[i] = spec;
        }
        return specs;
    }

    ColumnSpec buildColumnSpec(Class clazz, Field field) throws NoSuchMethodException {
        ExcelColumn colAnno = field.getAnnotation(ExcelColumn.class);
        var fieldName = field.getName();
        var getter = clazz.getMethod(getterName(fieldName));
        ColumnSpec spec;
        switch (getter.getReturnType().getName()) {
            case "java.lang.Integer":
            case "java.lang.Long": spec = new IntegerColumn(); break;
            case "java.math.BigDecimal":
            case "java.lang.Float":
            case "java.lang.Double": spec = buildFloatColumn(colAnno); break;
            case "java.time.LocalDate": spec = buildDateColumn(colAnno); break;
            case "java.time.LocalDateTime": spec = buildDateTimeColumn(colAnno); break;
            default: spec = new ColumnSpec();
        }
        spec.getter = getter;
        spec.init(colAnno, this);
        return spec;
    }

    FloatColumn buildFloatColumn(ExcelColumn colAnno) {
        var spec = new FloatColumn();
        return spec;
    }

    LocalDateColumn buildDateColumn(ExcelColumn colAnno) {
        var spec = new LocalDateColumn();
        spec.setFormat(colAnno.format().length() == 0 ? defaultDateFormat : colAnno.format());
        return spec;
    }

    LocalDateTimeColumn buildDateTimeColumn(ExcelColumn colAnno) {
        var spec = new LocalDateTimeColumn();
        spec.setFormat(colAnno.format().length() == 0 ? defaultDateTimeFormat : colAnno.format());
        return spec;
    }
}
