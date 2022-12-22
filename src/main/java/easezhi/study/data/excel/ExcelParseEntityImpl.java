package easezhi.study.data.excel;

public abstract class ExcelParseEntityImpl implements ExcelParseEntity{
    private Integer excelRowIndex;

    private String excelErrorMsg;

    @Override
    public void setExcelRowIndex(Integer row) {
        excelRowIndex = row;
    }

    public Integer getExcelRowIndex() {
        return excelRowIndex;
    }

    // 默认只记录第一次赋值的错误文本
    // 复杂功能需自行实现
    @Override
    public void setExcelBeanError(String msg) {
        if (getExcelBeanErrorMsg() == null) {
            excelErrorMsg = msg;
        }
    }

    public String getExcelBeanErrorMsg() {
        return excelErrorMsg;
    }
}
