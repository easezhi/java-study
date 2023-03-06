package study.cnbm.iti.model;

import easezhi.study.data.excel.ExcelColumnMapType;
import easezhi.study.data.excel.ExcelParseEntity;
import easezhi.study.data.excel.annotation.ExcelColumn;
import easezhi.study.data.excel.annotation.ExcelEntity;
import lombok.Data;

@Data
@ExcelEntity(columnMapType = ExcelColumnMapType.COLUMN_NAME)
public class PurchaseOrder implements ExcelParseEntity {
    @ExcelColumn("采购订单号")
    String poNo;

    @ExcelColumn("供应商订单号")
    String supplierOrder;

    @ExcelColumn("商务")
    String businessMan;
}
