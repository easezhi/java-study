package study.cnbm.iti.model;

import easezhi.study.data.excel.ExcelColumnMapType;
import easezhi.study.data.excel.ExcelParseEntity;
import easezhi.study.data.excel.annotation.ExcelColumn;
import easezhi.study.data.excel.annotation.ExcelEntity;
import lombok.Data;

@Data
@ExcelEntity(sheet = "发票", columnMapType = ExcelColumnMapType.COLUMN_NAME, checkTitle = false)
public class HwInvoiceItem implements ExcelParseEntity {

//    @ExcelColumn(value = "发票号码")
//    String invoiceNumber;

    @ExcelColumn(value = "行号")
    String itemNumber = "00010";

    @ExcelColumn(value = "税率")
    String taxRate;

    String itemType = "item";

    String itemDescription = "测试";

    String taxProductCode = "测试";

    String netAmount = "0";

    String taxAmount = "0";

    String totalAmount = "0";
}
