package study.cnbm.bean;

import com.alibaba.fastjson2.annotation.JSONField;
import easezhi.study.data.excel.annotation.ExcelColumn;
import easezhi.study.data.excel.annotation.ExcelEntity;
import lombok.Data;

@Data
@ExcelEntity(title = {"物料编码", "原厂物料编码", "产品线", "规格型号", "物料描述"})
public class Material {
    @ExcelColumn("物料编码")
    @JSONField(name = "MATNR")
    String matCode;

    @ExcelColumn("原厂物料编码")
    @JSONField(name = "BISMT")
    String originCode;

    // 物料类型
    @JSONField(name = "MTART")
    String matType;

    @ExcelColumn("产品线")
    @JSONField(name = "MATKL")
    String productLine;

    @ExcelColumn(value = "规格型号", width = 20)
    @JSONField(name = "ZZGKXH")
    String model;

    @ExcelColumn("原厂物料名称")
    @JSONField(name = "ZZCPMC")
    String originName;

    @ExcelColumn(value = "物料描述", width = 24)
    @JSONField(name = "MAKTX")
    String desc;
}
