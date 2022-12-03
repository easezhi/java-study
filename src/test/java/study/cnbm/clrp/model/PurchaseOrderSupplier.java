package study.cnbm.clrp.model;

import easezhi.study.data.excel.ExcelColumnMapType;
import easezhi.study.data.excel.ExcelParseEntity;
import easezhi.study.data.excel.annotation.ExcelColumn;
import easezhi.study.data.excel.annotation.ExcelEntity;
import lombok.Data;

@ExcelEntity(columnMapType = ExcelColumnMapType.COLUMN_NAME)
@Data
public class PurchaseOrderSupplier implements ExcelParseEntity {
    @ExcelColumn("LIFNR")
    String supplierId;

    @ExcelColumn("LIFNR_NAME")
    String name;

    @ExcelColumn("LIFNR_NAME2")
    String name2;

    public String getSupplier() {
        return name2 == null ? name : name + name2;
    }
}
