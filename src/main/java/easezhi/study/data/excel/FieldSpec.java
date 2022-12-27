package easezhi.study.data.excel;


import easezhi.study.data.excel.annotation.ExcelColumn;
import easezhi.study.data.excel.annotation.ExcelEntity;
import org.apache.poi.ss.usermodel.Cell;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import static easezhi.study.data.excel.ExcelUtil.setterName;
import static org.apache.poi.ss.usermodel.CellType.BLANK;

class FieldSpec {
    Method setter;

    String fieldName;

    Class<?> fieldType;

    String title;

    int colIndex;

    boolean required;

    int precision;

    int scale;

    Integer max;

    String format;

    boolean escapeCsvDelimiter;

    void specifyField(ExcelEntity excelAnno, ExcelColumn colAnno) {}

    final FieldBuilder<FieldSpec, ?> builder(Object bean, ExcelBeanError beanError, Cell cell) {
        var builder = newBuilder();
        builder.bean = bean;
        builder.beanError = beanError;
        builder.cell = cell;
        builder.spec = this;
        return builder;
    }

    <S extends FieldSpec> FieldBuilder<S, ?> newBuilder() {
        return null;
    }

    static <E extends ExcelParseEntity> FieldSpec genFieldSpec(Class<E> clazz, Field field) throws NoSuchMethodException {
        ExcelEntity excelAnno = clazz.getAnnotation(ExcelEntity.class);
        ExcelColumn colAnno = field.getAnnotation(ExcelColumn.class);
        var fieldName = field.getName();
        var fieldType = field.getType();
        var setter = clazz.getMethod(setterName(fieldName), fieldType);
        FieldSpec spec;
        if (fieldType == String.class) {
            // 字符串
            spec = new StringField();
        } else if (fieldType == Boolean.class) {
            spec = new BoolField();
        } else if (fieldType == Integer.class || fieldType == Long.class) {
            // 整数
            spec = new IntegerNumField();
        } else if (fieldType == BigDecimal.class || fieldType == Float.class || fieldType == Double.class) {
            // 小数
            spec = new FloatNumField();
        } else if (fieldType == LocalDate.class) {
            // 日期
            spec = new LocalDateField();
        } else if (fieldType == LocalDateTime.class) {
            // 时间
            spec = new LocalDateTimeField();
        } else {
            throw new RuntimeException("不支持的字段类型" + fieldType.getSimpleName());
        }

        // 从注解中获取字段类型、格式信息
        spec.setter = setter;
        spec.fieldName = fieldName;
        spec.fieldType = fieldType;
        spec.title = colAnno.value();
        spec.required = colAnno.required();
        spec.precision = colAnno.precision();
        spec.scale = colAnno.scale();
        if (colAnno.max() > 0) spec.max = colAnno.max();
        if (colAnno.format().length() > 0) spec.format = colAnno.format();
        spec.escapeCsvDelimiter = colAnno.escapeCsvDelimiter();

        // 具体类型特定的字段类型配置
        spec.specifyField(excelAnno, colAnno);
        return spec;
    }
}

class FieldBuilder<S extends FieldSpec, V> {

    Object bean;

    Cell cell;

    ExcelBeanError beanError;

    V value;

    V getValue() {
        return value;
    }

    S spec;

    final void addError(ExcelColumnError.ErrorType errorType) {
        var error = new ExcelColumnError(errorType, spec);
        beanError.columnErrors.add(error);
    }

    // 单元格解析入口函数
    final Object parse() throws Exception {
        // 没被编辑的单元格是 null，只编辑样式没有内容的类型是 BLANK
        if (cell == null || cell.getCellType().equals(BLANK)) {
            if (spec.required) addError(ExcelColumnError.ErrorType.REQUIRE);
            return null;
        }

        // 核心逻辑
        parseCellValue();
        var val = setBean();

        // 必输性检查
        if (val == null && spec.required) {
            addError(ExcelColumnError.ErrorType.REQUIRE);
        }
        return val;
    }

    void parseCellValue() {}

    // 字段值赋值到 bean 中
    V setBean() throws Exception {
        V val = getValue();
        if (val != null) {
            spec.setter.invoke(bean, val);
        }
        return val;
    }

}
