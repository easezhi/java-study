package easezhi.study.data.excel;

import easezhi.study.data.excel.annotation.ExcelColumn;
import org.apache.poi.ss.usermodel.Cell;

import static org.apache.poi.ss.usermodel.CellType.*;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

class ExcelUtil {

    static Map<String, Field> getAnnotatedFields(Class clazz) {
        Map<String, Field> fieldMap = new HashMap<>();
        var fields = clazz.getDeclaredFields();
        for (var field: fields) {
            var colAnno = field.getAnnotation(ExcelColumn.class);
            if (colAnno != null) {
                fieldMap.put(colAnno.value(), field);
            }
        }
        return fieldMap;
    }

    static boolean strEmpty(String str) {
        return str == null || str.length() == 0;
    }

    static String getterName(String fieldName) {
        return "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
    }

    static String setterName(String fieldName) {
        return "set" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
    }

    // 单元格是字符类型，包括字符类型公式
    static boolean cellIsString(Cell cell) {
        var cellType = cell.getCellType();
        return cellType.equals(STRING) ||
            (cellType.equals(FORMULA) && cell.getCachedFormulaResultType().equals(STRING));
    }

    // 单元格是数值类型，包括数值类型公式
    static boolean cellIsNumeric(Cell cell) {
        var cellType = cell.getCellType();
        return cellType.equals(NUMERIC) ||
            (cellType.equals(FORMULA) && cell.getCachedFormulaResultType().equals(NUMERIC));
    }

    static boolean cellIsBoolean(Cell cell) {
        var cellType = cell.getCellType();
        return cellType.equals(BOOLEAN) ||
            (cellType.equals(FORMULA) && cell.getCachedFormulaResultType().equals(BOOLEAN));
    }
}
