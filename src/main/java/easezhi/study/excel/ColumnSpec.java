package easezhi.study.excel;

import easezhi.study.excel.annotation.ExcelColumn;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRow;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

class ColumnSpec {
    ExcelColumn colAnno;

    Method getter;

    Method setter;

    int colIndex;

    String polyfill;

    Integer width;

    XSSFCellStyle cellStyle;

    void init(ExcelColumn colAnno, ExcelBuilder builder) {
        this.colAnno = colAnno;
        if (colAnno.polyfill().length() > 0) polyfill = colAnno.polyfill();

        width = colAnno.width() > 0 ? (Integer) colAnno.width() :
            colAnno.width() == 0 ? builder.defaultColumnWidth : null;
//        cellStyle = builder.workbook.createCellStyle();
    }

    void createCell(XSSFRow row, Object doc) throws InvocationTargetException, IllegalAccessException {
        var rawValue = getter.invoke(doc);
        if (rawValue == null) {
            if (polyfill != null) {
                fillTextCell(row.createCell(colIndex), polyfill);
            }
            return;
        }

        fillCell(row.createCell(colIndex), rawValue);
    }

    void fillCell(XSSFCell cell, Object rawValue) {
        cell.setCellValue(rawValue.toString());
    }

    final void fillTextCell(XSSFCell cell, String value) {
        cell.setCellValue(value);
    }

    final void fillNumericCell(XSSFCell cell, double value) {
        cell.setCellValue(value);
    }
}

class IntegerColumn extends ColumnSpec {

    void fillCell(XSSFCell cell, Object rawValue) {

        fillNumericCell(cell, ((Number)rawValue).longValue());
    }
}

class FloatColumn extends ColumnSpec {

    Integer scale;

    String format;

    void init(ExcelColumn colAnno, ExcelBuilder builder) {
        super.init(colAnno, builder);
        int scale = colAnno.scale();
        if (scale >= 0) {
            this.scale = scale;
            format = "%." + scale + "f";
        }
    }

    void fillCell(XSSFCell cell, Object rawValue) {
        double value = ((Number)rawValue).doubleValue();
        if (format == null) {
            fillNumericCell(cell, value);
        } else {
            var cellValue = String.format(format, value);
            fillTextCell(cell, cellValue);
        }
    }
}

class CommonDateColumn extends ColumnSpec {
    DateFormatter formatter;

    void setFormat (String format) {
        formatter = DateFormatter.getInstance(format);
    }
}

class LocalDateColumn extends CommonDateColumn {
    void fillCell(XSSFCell cell, Object rawValue) {
        var value = (LocalDate)rawValue;
        var cellValue = formatter.formatDate(value);
        fillTextCell(cell, cellValue);
    }
}

class LocalDateTimeColumn extends CommonDateColumn {
    void fillCell(XSSFCell cell, Object rawValue) {
        var value = (LocalDateTime)rawValue;
        var cellValue = formatter.formatDate(value);
        fillTextCell(cell, cellValue);
    }
}

class DateFormatter {
    static ZoneId timeZone;

    static {
        timeZone = ZoneId.systemDefault();
    }

    SimpleDateFormat formatter;

    static DateFormatter getInstance(String format) {
        DateFormatter inst = new DateFormatter();
        inst.formatter = new SimpleDateFormat(format);
        return inst;
    }

    String formatDate(LocalDate value) {
        Date date = Date.from(value.atStartOfDay(timeZone).toInstant());
        return formatter.format(date);
    }

    String formatDate(LocalDateTime value) {
        Date date = Date.from(value.atZone(timeZone).toInstant());
        return formatter.format(date);
    }

}
