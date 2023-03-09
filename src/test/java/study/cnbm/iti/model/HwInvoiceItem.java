package study.cnbm.iti.model;

import easezhi.study.data.excel.annotation.ExcelColumn;
import lombok.Data;

@Data
public class HwInvoiceItem {

    @ExcelColumn(value = "发票号码")
    String invoiceNumber;

    @ExcelColumn(value = "行号")
    String itemNumber;

    @ExcelColumn(value = "税率")
    String taxRate;
}
