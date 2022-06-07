package easezhi.study.excel;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.util.*;

import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import easezhi.study.excel.annotation.*;
import static easezhi.study.excel.ExcelUtil.*;

public class ExcelBuilder <E> {

    XSSFWorkbook workbook;

    String[] titles;

    String sheetName;

    ColumnSpec[] columnSpecs;

    String defaultDateFormat = "yyyy-MM-dd";
    String defaultDateTimeFormat = "yyyy-MM-dd HH:mm";

    Integer defaultColumnWidth;

    public ExcelBuilder<E> init(Class<E> clazz) throws NoSuchMethodException {
        workbook = new XSSFWorkbook();

        var excelAnno = clazz.getAnnotation(ExcelEntity.class);
        sheetName = excelAnno.sheet();
        titles = excelAnno.title();
        if (excelAnno.dateFormat().length() > 0) defaultDateFormat = excelAnno.dateFormat();
        if (excelAnno.dateTimeFormat().length() > 0) defaultDateTimeFormat = excelAnno.dateTimeFormat();
        if (excelAnno.width() > 0) defaultColumnWidth = excelAnno.width();

        columnSpecs = buildColumnSpecs(clazz, titles);
        return this;
    }

    public void build(OutputStream os, List<E> docList) throws Exception {
        var sheet = workbook.createSheet(sheetName);
        var titleRow = sheet.createRow(0);

        var titleFont = workbook.createFont();
        titleFont.setFontName("黑体");
        var titleStyle = workbook.createCellStyle();
        titleStyle.setAlignment(HorizontalAlignment.CENTER);
        titleStyle.setFont(titleFont);
        for (int i = 0; i < titles.length; i++) {
            var cell = titleRow.createCell(i);
            cell.setCellValue(titles[i]);
            cell.setCellStyle(titleStyle);

            var colSpec = columnSpecs[i];
            if (colSpec.width != null) {
                sheet.setColumnWidth(i, colSpec.width * 256); // 每一个单位是 1/256 个字符宽度
            }
        }

        for (int r = 0; r < docList.size(); r++) {
            var doc = docList.get(r);
            var row = sheet.createRow(r + 1);
            for (var spec: columnSpecs) {
                spec.createCell(row, doc);
            }
        }
        workbook.write(os);
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
