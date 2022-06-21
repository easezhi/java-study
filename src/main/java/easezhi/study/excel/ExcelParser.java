package easezhi.study.excel;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.*;

import easezhi.study.excel.annotation.ExcelColumn;
import easezhi.study.excel.annotation.ExcelEntity;
import easezhi.study.excel.ExcelColumnError.ErrorType;
import static easezhi.study.excel.ExcelUtil.*;

public class ExcelParser <E extends ExcelParseEntity> {

    Class<E> clazz;

    Constructor<E> constructor;

    String sheetName;

    String[] titles;

    int titleRow; // 标题所在行，从0开始
    int dataRow; // 数据所在行

    FieldSpec[] fieldSpecs;

    Map<ErrorType, String> errorMap;

    boolean simplifyErrorMsg;

    String defaultDateFormat = "yyyy-M-d";

    public static <E extends ExcelParseEntity> ExcelParser<E> parser(Class<E> clazz) throws Exception {
        return new ExcelParser<E>().init(clazz);
    }

    ExcelParser<E> init(Class<E> clazz) throws Exception {
        this.clazz = clazz;
        constructor = clazz.getConstructor();
        var excelAnno = clazz.getAnnotation(ExcelEntity.class);
        if (excelAnno.dateFormat().length() > 0) defaultDateFormat = excelAnno.dateFormat();

        simplifyErrorMsg = excelAnno.simplifyErrorMsg();
        sheetName = excelAnno.sheet();
        titles = excelAnno.title();
        titleRow = excelAnno.titleRow() - 1;
        if (excelAnno.dataRow() == 0) { // 不指定，默认值
            dataRow = titleRow + 1;
        } else {
            dataRow = excelAnno.dataRow() - 1;
        }
        fieldSpecs = buildFieldSpecs();
        return this;
    }

    public ExcelParser<E> setErrorMap(Map<ErrorType, String> errorMap) {
        this.errorMap = errorMap;
        return this;
    }

    public List<E> parse(InputStream is) throws Exception {
        var workbook = WorkbookFactory.create(is);
        Sheet sheet;
        if (sheetName.length() > 0) {
            sheet = workbook.getSheet(sheetName);
        } else {
            sheet = workbook.getSheetAt(0);
        }
        int totalRow = sheet.getLastRowNum();

        ArrayList<E> beans = new ArrayList<>();
        int lastEmptyRows = 0; // 最后若干空行，需要忽略掉
        for (int r = dataRow; r <= totalRow; r++) {
            var row = sheet.getRow(r);
            E bean = constructor.newInstance();
            ExcelBeanError beanError = bean.getExcelBeanError();
            if (beanError == null) {
                beanError = new ExcelBeanError();
                bean.setExcelBeanError(beanError);
            }
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

        if (lastEmptyRows > 0) {
            beans.subList(beans.size() - lastEmptyRows, beans.size()).clear(); // 删掉最后的空行
        }

        return beans;
    }

    FieldSpec[] buildFieldSpecs() throws NoSuchMethodException {
        Map<String, Field> fieldMap = ExcelUtil.getAnnotatedFields(clazz);
        List<FieldSpec> specs = new ArrayList<>(titles.length);
        for (var i = 0; i < titles.length; i++) {
            var title = titles[i];
            var field = fieldMap.get(title);
            if (field == null) continue;

            var spec = buildFieldSpec(clazz, field);
            spec.colIndex = i;
            specs.add(spec);
        }
        return specs.toArray(new FieldSpec[]{});
    }

    FieldSpec buildFieldSpec(Class clazz, Field field) throws NoSuchMethodException {
        ExcelColumn colAnno = field.getAnnotation(ExcelColumn.class);
        var fieldName = field.getName();
        var fieldType = field.getType();
        var setter = clazz.getMethod(setterName(fieldName), fieldType);
        FieldSpec spec;
        switch (fieldType.getName()) {
            case "java.lang.String": spec = new StringField(); break;
            case "java.lang.Integer":
            case "java.lang.Long":  spec = new IntegerField(); break;
            case "java.math.BigDecimal":
            case "java.lang.Float":
            case "java.lang.Double": spec = new DoubleField(); break;
//            case "java.time.LocalDate": spec = buildDateColumn(colAnno); break;
            case "java.time.LocalDateTime": spec = new LocalDateTimeField(); break;
            default: spec = new FieldSpec();
        }
        spec.setter = setter;
        spec.fieldName = fieldName;
        spec.fieldType = fieldType;
        spec.title = colAnno.value();
        spec.init(colAnno, this);
        return spec;
    }
}
