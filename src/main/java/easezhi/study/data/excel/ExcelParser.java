package easezhi.study.data.excel;

import easezhi.study.data.excel.annotation.ExcelEntity;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import java.io.FileInputStream;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ExcelParser <E extends ExcelParseEntity> {

    Class<E> clazz;

    Constructor<E> constructor;

    String sheetName;

    String[] titles;

    boolean checkTitle;

    ExcelColumnMapType columnMapType;

    int titleRowIndex; // 标题所在行，从0开始
    int dataRowIndex; // 数据所在行

    FieldSpec[] fieldSpecs;

    Map<ExcelColumnError.ErrorType, String> errorMap;

    boolean simplifyErrorMsg;

    String defaultDateFormat = "yyyy-M-d";

    public static <E extends ExcelParseEntity> ExcelParser<E> parser(Class<E> clazz) throws Exception {
        var parser = new ExcelParser<E>().init(clazz);
        if (parser.columnMapType == ExcelColumnMapType.TITLE_LIST) {
            if (parser.titles.length == 0) {
                throw new RuntimeException("必需指定表头");
            }
            parser.fieldSpecs = parser.buildFieldSpecsByTitleList();
        }
        return parser;
    }

    ExcelParser<E> init(Class<E> clazz) throws Exception {
        this.clazz = clazz;
        constructor = clazz.getConstructor();
        var excelAnno = clazz.getAnnotation(ExcelEntity.class);
        if (excelAnno.dateFormat().length() > 0) defaultDateFormat = excelAnno.dateFormat();

        simplifyErrorMsg = excelAnno.simplifyErrorMsg();
        sheetName = excelAnno.sheet();
        titles = excelAnno.title();
        columnMapType = excelAnno.columnMapType();
        checkTitle = excelAnno.checkTitle();
        titleRowIndex = excelAnno.titleRow() - 1;
        if (excelAnno.dataRow() == 0) { // 不指定，默认值
            dataRowIndex = titleRowIndex + 1;
        } else {
            dataRowIndex = excelAnno.dataRow() - 1;
        }
        return this;
    }

    public ExcelParser<E> setErrorMap(Map<ExcelColumnError.ErrorType, String> errorMap) {
        this.errorMap = errorMap;
        return this;
    }

    public List<E> parseExcelFile(String filePath) {
        try (var is = new FileInputStream(filePath)) {
            return parse(is);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public List<E> parse(InputStream is) throws Exception {
        var workbook = WorkbookFactory.create(is);
        Sheet sheet;
        if (sheetName.length() > 0) {
            sheet = workbook.getSheet(sheetName);
        } else {
            sheet = workbook.getSheetAt(0);
        }

        // 初始化列与字段映射
        var titleRow = sheet.getRow(titleRowIndex);
        if (columnMapType == ExcelColumnMapType.COLUMN_NAME) {
            fieldSpecs = buildFieldSpecsByColumnName(titleRow);
        } else if (columnMapType == ExcelColumnMapType.COLUMN_INDEX) {
            fieldSpecs = buildFieldSpecsByColumnIndex(titleRow);
        }

        int totalRow = sheet.getLastRowNum();
        ArrayList<E> beans = new ArrayList<>();
        int lastEmptyRows = 0; // 最后若干空行，需要忽略掉
        for (int r = dataRowIndex; r <= totalRow; r++) {
            var row = sheet.getRow(r);
            E bean = constructor.newInstance();
            bean.setExcelRowIndex(r + 1);
            ExcelBeanError beanError = bean.getExcelBeanError();
            if (beanError == null) {
                beanError = new ExcelBeanError();
                bean.setExcelBeanError(beanError);
            }

            // 如果所有字段都是 null，视为空行
            boolean rowEmpty = true;
            for (var spec: fieldSpecs) {
                var cell = row.getCell(spec.colIndex);
                Object value = spec.builder(bean, beanError, cell).parse();
                if (value != null) rowEmpty = false;
            }
            beans.add(bean);

            if (rowEmpty) {
                lastEmptyRows++;
            } else {
                lastEmptyRows = 0;
            }

            // 处理导入数据有误的错误信息
            if (simplifyErrorMsg) {
                bean.setExcelBeanError(beanError.getSingleError(errorMap));
            } else {
                bean.setExcelBeanError(errorMap == null ? beanError.getJoinedError() : beanError.getJoinedError(errorMap));
            }
        }

        // 删掉最后的空行
        if (lastEmptyRows > 0) {
            beans.subList(beans.size() - lastEmptyRows, beans.size()).clear();
        }

        return beans;
    }

    FieldSpec[] buildFieldSpecsByTitleList() throws NoSuchMethodException {
        Map<String, Field> fieldMap = ExcelUtil.getAnnotatedFieldsMap(clazz);
        List<FieldSpec> specs = new ArrayList<>(titles.length);
        for (var i = 0; i < titles.length; i++) {
            var title = titles[i];
            var field = fieldMap.get(title);
            if (field == null) continue;

            var spec = FieldSpec.genFieldSpec(clazz, field);
            spec.colIndex = i;
            specs.add(spec);
        }
        return specs.toArray(new FieldSpec[0]);
    }

    FieldSpec[] buildFieldSpecsByColumnName(Row titleRow) throws NoSuchMethodException {
        Map<String, Field> fieldMap = ExcelUtil.getAnnotatedFieldsMap(clazz);
        var specs = new ArrayList<FieldSpec>(fieldMap.size());

        var minIdx = titleRow.getFirstCellNum();
        var maxIdx = titleRow.getLastCellNum();
        var titles = new String[maxIdx];
        for (var i = minIdx; i < maxIdx; i++) {
            titles[i] = ExcelUtil.cellGetTextValue(titleRow.getCell(i));
        }

        var missedTitles = new ArrayList<String>();
        for (var entry: fieldMap.entrySet()) {
            var name = entry.getKey();
            var field = entry.getValue();
            var idx = findTitleIndex(titles, name);
            if (idx >= 0) {
                var spec = FieldSpec.genFieldSpec(clazz, field);
                spec.colIndex = idx;
                specs.add(spec);
            } else {
                missedTitles.add(name);
            }
        }
        if (checkTitle && !missedTitles.isEmpty()) {
            throw new RuntimeException("Excel文件缺少列：" + String.join("、", missedTitles));
        }
        return specs.toArray(new FieldSpec[0]);
    }

    FieldSpec[] buildFieldSpecsByColumnIndex(Row titleRow) {
        return null;
    }

    int findTitleIndex(String[] titles, String title) {
        for (int i = 0; i < titles.length; i++) {
            if (title.equals(titles[i])) {
                return i;
            }
        }
        return -1;
    }
}
