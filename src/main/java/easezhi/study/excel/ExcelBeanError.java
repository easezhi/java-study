package easezhi.study.excel;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import easezhi.study.excel.ExcelColumnError.ErrorType;
import static easezhi.study.excel.ExcelColumnError.ErrorType.*;

public class ExcelBeanError {
    public final List<ExcelColumnError> columnErrors;

    public ExcelBeanError() {
        columnErrors = new ArrayList<>();
    }

    public String getSingleError(Map<ErrorType, String> errorMap) {
        if (columnErrors.isEmpty()) return null;

        String msg = null;
        for (var errorType: ErrorType.values()) {
            for (var error: columnErrors) {
                if (error.errorType.equals(errorType)) {
                    msg = errorMap == null ? error.getDefaultErrorMsg() : getErrorByMap(errorMap, error);
                    break;
                }
            }
            if (msg != null) break;
        }
        return msg;
    }

    public String getJoinedError() {
        if (columnErrors.isEmpty()) return null;

        return columnErrors.stream().map(ExcelColumnError::getDefaultErrorMsg).collect(Collectors.joining("；"));
    }

    public String getJoinedError(Map<ErrorType, String> errorMap) {
        if (columnErrors.isEmpty()) return null;

        List<String> msgs = new ArrayList<>(columnErrors.size());
        for (var error: columnErrors) {
            msgs.add(getErrorByMap(errorMap, error));
        }
        return String.join("；", msgs);
    }

    static String getErrorByMap(Map<ErrorType, String> errorMap, ExcelColumnError error) {
        return errorMap.containsKey(error.errorType) ?
            errorMap.get(error.errorType) : // 如果要支持更复杂的自定义错误文本，还需优化
            error.getDefaultErrorMsg();
    }
}
