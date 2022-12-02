package easezhi.study.data.excel;

import easezhi.study.data.excel.annotation.ExcelColumn;
import easezhi.study.data.excel.annotation.ExcelEntity;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.util.NumberToTextConverter;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoField;

// 支持 true\false t\f 不区分大小写，其他值都视为 null
class BoolField extends FieldSpec {
    @Override
    BoolBuilder newBuilder() {
        var builder = new BoolBuilder();
        builder.spec = this;
        return builder;
    }
}

class BoolBuilder extends FieldBuilder<BoolField, Boolean> {
    void parseCellValue() {
        if (ExcelUtil.cellIsBoolean(cell)) {
            value = cell.getBooleanCellValue();
        } else if (ExcelUtil.cellIsString(cell)) {
            var str = cell.getStringCellValue().trim().toLowerCase();
            if (str.equals("true") || str.equals("t")) {
                value = true;
            } else if (str.equals("false") || str.equals("f")) {
                value = false;
            }
        }
    }
}

class StringField extends FieldSpec {
    @Override
    StringBuilder newBuilder() {
        var builder = new StringBuilder();
        builder.spec = this;
        return builder;
    }
}

class StringBuilder extends FieldBuilder<StringField, String> {

    @Override
    void parseCellValue() {
        if (ExcelUtil.cellIsString(cell)) {
            value = cell.getStringCellValue().trim();
        } else if (ExcelUtil.cellIsNumeric(cell)) {
            value = NumberToTextConverter.toText(cell.getNumericCellValue()); // 数字单元格最多15位有效数字
        }
        if (ExcelUtil.strEmpty(value)) {
            value = null; // 用户没有编辑单元格、单元格内容是空白字符，统一按 null 处理

        } else if (spec.max != null && value.length() > spec.max) { // 超长
            addError(ExcelColumnError.ErrorType.MAX_LENGTH);
        }
    }
}

class NumericField extends FieldSpec {

    int intPrec; // 整数位数

    long intPrecLimit;

    void specifyField(ExcelEntity excelAnno, ExcelColumn colAnno) {
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

class IntegerNumField extends NumericField {

    ExcelColumnError.ErrorType checkValue(Long value) {
        if (value != null) {
            if (!checkIntPrec(value)) return ExcelColumnError.ErrorType.INTEGER_PRECISION;
        }
        return null;
    }

    IntegerNumBuilder<IntegerNumField, ? extends Number> newBuilder() {
        IntegerNumBuilder<IntegerNumField, ? extends Number> builder;
        if (fieldType == Integer.class) {
            builder = new IntegerBuilder();
        } else {
            builder = new LongBuilder();
        }
        builder.spec = this;
        return builder;
    }
}

class IntegerNumBuilder<S extends IntegerNumField, V extends Number> extends FieldBuilder<S, V> {
    Long longValue;

    void parseCellValue() {
        if (ExcelUtil.cellIsNumeric(cell)) {
            var dv = cell.getNumericCellValue();
            var lv = ((Double)dv).longValue();
            if (dv != lv) {
                addError(ExcelColumnError.ErrorType.INTEGER_PRECISION); // 有小数
                return;
            }
            longValue = lv;
        } else if (ExcelUtil.cellIsString(cell)) {
            String str = cell.getStringCellValue().trim();
            if (str.length() > 0) {
                try {
                    longValue = Long.parseLong(str);
                } catch (NumberFormatException e) {
                    addError(ExcelColumnError.ErrorType.NUMBER_FORMAT);
                    return;
                }
            }
        }
        var errorType = spec.checkValue(longValue);
        if (errorType != null) {
            addError(errorType);
        }
    }
}

class LongBuilder extends IntegerNumBuilder<IntegerNumField, Long> {
    Long getValue() {
        return longValue;
    }
}

class IntegerBuilder extends IntegerNumBuilder<IntegerNumField, Integer> {
    void parseCellValue() {
        super.parseCellValue();

        if (longValue != null) {
            if (longValue > Integer.MAX_VALUE || longValue < Integer.MIN_VALUE) {
                addError(ExcelColumnError.ErrorType.NUMBER_FORMAT);
            } else {
                value = longValue.intValue();
            }
        }
    }
}

class FloatNumField extends NumericField {

    ExcelColumnError.ErrorType checkValue(Double value) {
        if (value != null) {
            if (!checkFloatPrec(value)) {
                return ExcelColumnError.ErrorType.FLOAT_PRECISION;
            }
        }
        return null;
    }

    FloatNumBuilder<FloatNumField, ? extends Number> newBuilder() {
        FloatNumBuilder<FloatNumField, ? extends Number> builder;
        if (fieldType == Float.class) {
            builder = new FloatBuilder();
        } else if (fieldType == BigDecimal.class) {
            builder = new BigDecimalBuilder();
        } else {
            builder = new DoubleBuilder();
        }
        builder.spec = this;
        return builder;
    }
}

class FloatNumBuilder<S extends FloatNumField, V extends Number> extends FieldBuilder<S, V> {
    Double doubleValue;

    void parseCellValue() {
        if (ExcelUtil.cellIsNumeric(cell)) {
            doubleValue = cell.getNumericCellValue();
        } else if (ExcelUtil.cellIsString(cell)) {
            String str = cell.getStringCellValue().trim();
            if (str.length() > 0) {
                try {
                    doubleValue = Double.parseDouble(str);
                } catch (NumberFormatException e) {
                    addError(ExcelColumnError.ErrorType.NUMBER_FORMAT);
                    return;
                }
            }
        }
        var errorType = spec.checkValue(doubleValue);
        if (errorType != null) {
            addError(errorType);
        }
    }
}

class DoubleBuilder extends FloatNumBuilder<FloatNumField, Double> {
    Double getValue() {
        return doubleValue;
    }
}

class FloatBuilder extends FloatNumBuilder<FloatNumField, Float> {
    Float getValue() {
        return doubleValue == null ? null : doubleValue.floatValue();
    }
}

// 如果要支持更高精度，还得优化
class BigDecimalBuilder extends FloatNumBuilder<FloatNumField, BigDecimal> {
    BigDecimal getValue() {
        return doubleValue == null ? null : BigDecimal.valueOf(doubleValue);
    }
}

class LocalTemporalField extends FieldSpec {
    DateTimeFormatter formatter;

    void specifyField(ExcelEntity excelAnno, ExcelColumn colAnno) {
        formatter = new DateTimeFormatterBuilder()
            .append(DateTimeFormatter.ofPattern(format))
            .parseDefaulting(ChronoField.MONTH_OF_YEAR, 1)
            .parseDefaulting(ChronoField.DAY_OF_MONTH, 1)
            .parseDefaulting(ChronoField.HOUR_OF_DAY, 0)
            .toFormatter();
    }
}

class LocalTemporalBuilder<S extends LocalTemporalField, V> extends FieldBuilder<S, V> {

    LocalDateTime localTimeValue;

    @Override
    void parseCellValue() {
        localTimeValue = parseLocalTime();
    }

    LocalDateTime parseLocalTime() {
        LocalDateTime val = null;

        if (ExcelUtil.cellIsNumeric(cell)) {
            if (DateUtil.isCellDateFormatted(cell)) {
                if (cell.getNumericCellValue() >= 2958466) { // 日期达到 10000/1/1
                    addError(ExcelColumnError.ErrorType.DATE_FORMAT_STRING);
                    return null;
                }
                val = cell.getLocalDateTimeCellValue();
            } else {
                addError(ExcelColumnError.ErrorType.DATE_FORMAT_STRING);
                return null;
            }
        } else if (ExcelUtil.cellIsString(cell)) {
            var str = cell.getStringCellValue().trim();
            if (str.length() > 0) {
                try {
                    val = LocalDateTime.parse(str, spec.formatter);
                } catch (DateTimeParseException e) {
                    addError(ExcelColumnError.ErrorType.DATE_FORMAT_STRING); // 字符串格式有误
                    return null;
                }
            }
        }
        return val;
    }
}

class LocalDateField extends LocalTemporalField {
    static private final String defaultDateFormat = "yyyy-M-d"; // 支持一位或两位的月、日

    void specifyField(ExcelEntity excelAnno, ExcelColumn colAnno) {
        // ExcelColumn 没有指定日期格式，则取值 ExcelEntity 设置的全局格式，或默认格式
        if (format == null) {
            format = excelAnno.dateFormat().length() > 0 ? excelAnno.dateFormat() : defaultDateFormat;
        }
        super.specifyField(excelAnno, colAnno);
    }

    LocalDateBuilder newBuilder() {
        LocalDateBuilder builder = new LocalDateBuilder();
        builder.spec = this;
        return builder;
    }
}

class LocalDateBuilder extends LocalTemporalBuilder<LocalDateField, LocalDate> {
    LocalDate getValue() {
        return localTimeValue == null ? null : localTimeValue.toLocalDate();
    }
}

class LocalDateTimeField extends LocalTemporalField {
    static private final String defaultDateTimeFormat = "yyyy-M-d HH:mm:ss";

    void specifyField(ExcelEntity excelAnno, ExcelColumn colAnno) {
        if (format == null) {
            format = excelAnno.dateTimeFormat().length() > 0 ? excelAnno.dateTimeFormat() : defaultDateTimeFormat;
        }
        super.specifyField(excelAnno, colAnno);
    }

    LocalDateTimeBuilder newBuilder() {
        LocalDateTimeBuilder builder = new LocalDateTimeBuilder();
        builder.spec = this;
        return builder;
    }
}

class LocalDateTimeBuilder extends LocalTemporalBuilder<LocalDateTimeField, LocalDateTime> {

    LocalDateTime getValue() {
        return localTimeValue;
    }
}
