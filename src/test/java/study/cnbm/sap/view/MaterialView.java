package study.cnbm.sap.view;

import easezhi.study.data.excel.annotation.ExcelColumn;
import lombok.Data;
import study.cnbm.sap.annotation.SapField;

@Data
public class MaterialView {
    @ExcelColumn("物料编码")
    @SapField(fieldType = "MATNR")
    String matCode;

    @ExcelColumn("原厂物料编码")
    @SapField(fieldType = "BISMT")
    String originCode;

    // 物料类型
    @SapField(fieldType = "MTART")
    String matType;

    @ExcelColumn("产品线")
    @SapField(fieldType = "MATKL")
    String productLine;

    @ExcelColumn(value = "规格型号", width = 20)
    @SapField(fieldType = "ZGKXH")
    String model;

    @ExcelColumn("原厂物料名称")
    @SapField(fieldType = "ZZCPMC")
    String originName;

    @ExcelColumn(value = "物料描述", width = 24)
    @SapField(fieldType = "MAKTX")
    String desc;
}
