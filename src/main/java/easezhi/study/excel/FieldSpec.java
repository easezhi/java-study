package easezhi.study.excel;

import easezhi.study.excel.annotation.ExcelColumn;
import static easezhi.study.excel.ExcelColumnError.ErrorType;
import static easezhi.study.excel.ExcelColumnError.ErrorType.*;
import static easezhi.study.excel.ExcelUtil.*;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DateUtil;

import static org.apache.poi.ss.usermodel.CellType.*;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoField;

/**
 * 非线程安全
 */
class FieldSpec <E extends ExcelParseEntity> {
    Method setter;

    String fieldName;

    Class fieldType;

    String title;

    int colIndex;

    boolean required;

    int precision;

    int scale;

    Integer max;

    String format;

    void init(ExcelColumn colAnno, ExcelParser parser) {
        required = colAnno.required();
        precision = colAnno.precision();
        scale = colAnno.scale();
        if (colAnno.max() > 0) max = colAnno.max();
        format = colAnno.format().length() > 0 ? colAnno.format() : parser.defaultDateFormat;
    }

    FieldBuilder builder(E bean, ExcelBeanError beanError, Cell cell) {
        FieldBuilder builder = newBuilder();
        builder.spec = this; // 即使子类重写了 spec 属性，这里也需要赋值
        builder.bean = bean;
        builder.beanError = beanError;
        builder.cell = cell;
        return builder;
    }

    FieldBuilder newBuilder() {
        return new FieldBuilder();
    }
}

class FieldBuilder <E extends ExcelParseEntity> {

    E bean;

    Cell cell;

    FieldSpec spec;

    ExcelBeanError beanError;

    // 单元格解析入口函数
    final Object parse() throws Exception {
        // 没被编辑的单元格是 null，只编辑样式没有内容的类型是 BLANK
        if (cell == null || cell.getCellType().equals(BLANK)) {
            if (spec.required) addError(REQUIRE);
            return null;
        }
        return parseCell();
    }

    Object parseCell() throws Exception {
        return null;
    }

    void addError(ErrorType errorType) {
        var error = new ExcelColumnError(errorType, spec);
        beanError.columnErrors.add(error);
    }
}

class StringField extends FieldSpec {
    FieldBuilder newBuilder() {
        return new StringBuilder();
    }
}

class StringBuilder extends FieldBuilder {
    String value;

    String parseCell() throws Exception {
        getCellValue();
        if (value != null) {
            spec.setter.invoke(bean, value);
        }
        return value;
    }

    void getCellValue() {
        if (cellIsString(cell)) {
            value = cell.getStringCellValue().trim();
        } else if (cellIsNumeric(cell)) {
            // 数字单元格，直接 double 转 String，会有 .0 后缀
            value = BigDecimal.valueOf(cell.getNumericCellValue()).stripTrailingZeros().toPlainString();
        }
        if (strEmpty(value)) {
            value = null; // 用户没有编辑单元格、单元格内容是空白字符，统一按 null 处理
            if (spec.required) { // 必输
                addError(REQUIRE);
            }
        } else if (spec.max != null && value.length() > spec.max) { // 超长
            addError(MAX_LENGTH);
        }
    }
}

class NumericField extends FieldSpec {

    int intPrec; // 整数位数

    long intPrecLimit;

    void init(ExcelColumn colAnno, ExcelParser parser) {
        super.init(colAnno, parser);
        intPrec = colAnno.precision() - colAnno.scale();
        scale = colAnno.scale();
        intPrecLimit = ((Double)Math.pow(10, intPrec)).longValue();
    }

    // 精确度检查算法待优化
    boolean checkIntPrec(long value) {
        return Math.abs(value) < intPrecLimit;
    }

    boolean checkFloatPrec(double value) {
        var av = Math.abs(value);
        var lv = ((Double)value).longValue();
        if (!checkIntPrec(lv)) return false;
        if (av == lv) return true; // 没有小数部分

        var fv = av - lv;
        var bd = new BigDecimal(fv).setScale(scale + 2, RoundingMode.HALF_UP).stripTrailingZeros(); // 舍掉长尾小数
        var fs = String.valueOf(bd).substring(2); // 小数部分的字符串表示
        return fs.length() <= scale;
    }
}

class IntegerField extends NumericField {
    void init(ExcelColumn colAnno, ExcelParser parser) {
        super.init(colAnno, parser);
    }

    ErrorType checkValue(Long value) {
        if (value == null) {
            if (required) return REQUIRE;
        } else {
            if (!checkIntPrec(value)) return INTEGER_PRECISION;
        }
        return null;
    }

    FieldBuilder newBuilder() {
        LongBuilder builder;
        if (fieldType.equals(Integer.class)) {
            builder = new IntegerBuilder();
        } else {
            builder = new LongBuilder();
        }
        builder.spec = this;
        return builder;
    }
}

class LongBuilder extends FieldBuilder {

    IntegerField spec;

    Long value;

    Long parseCell() throws Exception {
        getCellValue();
        setBean();
        return value;
    }

    void setBean() throws Exception {
        if (value != null) {
            spec.setter.invoke(bean, value);
        }
    }

    void getCellValue() {
        if (cellIsNumeric(cell)) {
            var dv = cell.getNumericCellValue();
            var lv = ((Double)dv).longValue();
            if (dv != lv) {
                addError(INTEGER_PRECISION); // 有小数
                return;
            }
            value = lv;
        } else if (cellIsString(cell)) {
            String str = cell.getStringCellValue().trim();
            if (str.length() > 0) {
                try {
                    value = Long.parseLong(str);
                } catch (NumberFormatException e) {
                    addError(NUMBER_FORMAT);
                    return;
                }
            }
        }
        var errorType = spec.checkValue(value);
        if (errorType != null) {
            addError(errorType);
        }
    }
}

class IntegerBuilder extends LongBuilder {
    void setBean() throws Exception {
        if (value != null) {
            if (value > Integer.MAX_VALUE || value < Integer.MIN_VALUE) {
                addError(NUMBER_FORMAT);
                return;
            }
            spec.setter.invoke(bean, value.intValue());
        }
    }
}

class DoubleField extends NumericField {

    ErrorType checkValue(Double value) {
        if (value == null) {
            if (required) return REQUIRE;
        } else if (!checkFloatPrec(value)) {
            return FLOAT_PRECISION;
        }
        return null;
    }

    FieldBuilder newBuilder() {
        DoubleBuild builder;
        if (fieldType.equals(Float.class)) {
            builder = new FloatBuild();
        } else if (fieldType.equals(BigDecimal.class)) {
            builder = new BigDecimalBuild();
        } else {
            builder = new DoubleBuild();
        }
        builder.spec = this;
        return builder;
    }
}

class DoubleBuild extends FieldBuilder {

    DoubleField spec;

    Double value;

    Double parseCell() throws Exception {
        getCellValue();
        setBean();
        return value;
    }

    void setBean() throws Exception {
        spec.setter.invoke(bean, value);
    }

    void getCellValue() {
        if (cellIsNumeric(cell)) {
            value = cell.getNumericCellValue();
        } else if (cellIsString(cell)) {
            String str = cell.getStringCellValue().trim();
            if (str.length() > 0) {
                try {
                    value = Double.parseDouble(str);
                } catch (NumberFormatException e) {
                    addError(NUMBER_FORMAT);
                    return;
                }
            }
        }
        var errorType = spec.checkValue(value);
        if (errorType != null) {
            addError(errorType);
        }
    }
}

class FloatBuild extends DoubleBuild {
    void setBean() throws Exception {
        spec.setter.invoke(bean, value.floatValue());
    }
}

// 如果要支持更高精度，还得优化
class BigDecimalBuild extends DoubleBuild {
    void setBean() throws Exception {
        if (value != null) {
            spec.setter.invoke(bean, BigDecimal.valueOf(value));
        }
    }
}

class LocalDateTimeField extends FieldSpec {

    DateTimeFormatter formatter;

    void init(ExcelColumn colAnno, ExcelParser parser) {
        super.init(colAnno, parser);
        formatter = new DateTimeFormatterBuilder()
            .append(DateTimeFormatter.ofPattern(format))
            .parseDefaulting(ChronoField.MONTH_OF_YEAR, 1)
            .parseDefaulting(ChronoField.DAY_OF_MONTH, 1)
            .parseDefaulting(ChronoField.HOUR_OF_DAY, 0)
            .toFormatter();
    }

    FieldBuilder newBuilder() {
        return new LocalDateTimeBuild(this);
    }
}

class LocalDateTimeBuild extends FieldBuilder {

    LocalDateTimeField spec;

    LocalDateTime value;

    LocalDateTimeBuild(LocalDateTimeField spec) {
        this.spec = spec;
    }

    LocalDateTime parseCell() throws Exception {
        getCellValue();
        setBean();
        return value;
    }

    void getCellValue() {
        if (cellIsNumeric(cell)) {
            if (DateUtil.isCellDateFormatted(cell)) {
                if (cell.getNumericCellValue() >= 2958466) { // 日期达到 10000/1/1
                    addError(DATE_FORMAT_STRING);
                    return;
                }
                value = cell.getLocalDateTimeCellValue();
            } else {
                addError(DATE_FORMAT_STRING);
                return;
            }
        } else if (cellIsString(cell)) {
            var str = cell.getStringCellValue().trim();
            if (str.length() > 0) {
                try {
                    value = LocalDateTime.parse(str, spec.formatter);
                } catch (DateTimeParseException e) {
                    addError(DATE_FORMAT_STRING); // 字符串格式有误
                    return;
                }
            }
        }
        if (spec.required && value == null) {
            addError(REQUIRE);
        }
    }

    void setBean() throws Exception {
        if (value != null) {
            spec.setter.invoke(bean, value);
        }
    }
}
