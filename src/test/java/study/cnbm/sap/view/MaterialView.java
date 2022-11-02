package study.cnbm.sap.view;

import com.alibaba.fastjson2.annotation.JSONField;
import easezhi.study.data.excel.annotation.ExcelColumn;
import lombok.Data;
import study.cnbm.sap.SapViewField;

@Data
public class MaterialView {
    @ExcelColumn("物料编码")
    @SapViewField(fieldType = "MATNR")
    String matCode;

    @ExcelColumn("原厂物料编码")
    @SapViewField(fieldType = "BISMT")
    String originCode;

    // 物料类型
    @SapViewField(fieldType = "MTART")
    String matType;

    @ExcelColumn("产品线")
    @SapViewField(fieldType = "MATKL")
    String productLine;

    @ExcelColumn(value = "规格型号", width = 20)
    @SapViewField(fieldType = "ZGKXH")
    String model;

    @ExcelColumn("原厂物料名称")
    @SapViewField(fieldType = "ZZCPMC")
    String originName;

    @ExcelColumn(value = "物料描述", width = 24)
    @SapViewField(fieldType = "MAKTX")
    String desc;
}
