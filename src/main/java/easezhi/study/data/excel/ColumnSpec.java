package easezhi.study.data.excel;

import easezhi.study.data.excel.annotation.ExcelColumn;
import org.apache.poi.ss.usermodel.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.RoundingMode;
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

    HorizontalAlignment align;

    CellStyle cellStyle;

    void init(ExcelColumn colAnno, ExcelBuilder builder) {
        this.colAnno = colAnno;
        if (colAnno.polyfill().length() > 0) polyfill = colAnno.polyfill();

        if (colAnno.width() > 0) {
            width = colAnno.width();
        } else if (colAnno.width() == 0 && builder.defaultColumnWidth != null) {
            width = colAnno.value().length() * 2 + 2; // 按汉字字符计算宽度
            if (width < builder.defaultColumnWidth) width = builder.defaultColumnWidth;
        }

        switch (colAnno.align()) {
            case "left": align = HorizontalAlignment.LEFT; break;
            case "center": align = HorizontalAlignment.CENTER; break;
            case "right": align = HorizontalAlignment.RIGHT; break;
        }

        if (align != null) {
            cellStyle = builder.workbook.createCellStyle();
            cellStyle.setAlignment(align);
            cellStyle.setVerticalAlignment(VerticalAlignment.CENTER); // 如果不设置，默认是居下对齐
        }
    }

    void createCell(Row row, Object doc) throws InvocationTargetException, IllegalAccessException {
        var cell = row.createCell(colIndex);
        if (cellStyle != null) {
            cell.setCellStyle(cellStyle);
        }

        Object rawValue = getter.invoke(doc);
        if (rawValue == null) {
            if (polyfill != null) {
                fillTextCell(cell, polyfill);
            }
            return;
        }

        fillCell(cell, rawValue);
    }

    void fillCell(Cell cell, Object rawValue) {
        cell.setCellValue(rawValue.toString());
    }

    final void fillTextCell(Cell cell, String value) {
        cell.setCellValue(value);
    }

    final void fillNumericCell(Cell cell, double value) {
        cell.setCellValue(value);
    }
}

class IntegerColumn extends ColumnSpec {

    void fillCell(Cell cell, Object rawValue) {

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

    void fillCell(Cell cell, Object rawValue) {
        BigDecimal dec;
        if (rawValue instanceof BigDecimal) {
            dec = (BigDecimal) rawValue;
        } else {
            dec = BigDecimal.valueOf(((Number)rawValue).doubleValue());
        }
        if (scale != null) {
            dec = dec.setScale(scale, RoundingMode.HALF_UP).stripTrailingZeros();
        }
        if (dec.scale() == 0) {
            fillNumericCell(cell, dec.longValue());
        } else {
            fillNumericCell(cell, dec.doubleValue());
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
    void fillCell(Cell cell, Object rawValue) {
        var value = (LocalDate)rawValue;
        var cellValue = formatter.formatDate(value);
        fillTextCell(cell, cellValue);
    }
}

class LocalDateTimeColumn extends CommonDateColumn {
    void fillCell(Cell cell, Object rawValue) {
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
