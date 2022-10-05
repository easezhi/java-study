package easezhi.study.data.excel;

public interface ExcelParseEntity {

    default ExcelBeanError getExcelBeanError() {
        return null;
    }

    default void setExcelBeanError(ExcelBeanError beanError) {}

    default void setExcelRowIndex(int row) {}

    default void setExcelBeanError(String msg) {}
}
