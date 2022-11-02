package study.cnbm;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.PropertyNamingStrategy;
import com.alibaba.fastjson2.annotation.JSONType;
import easezhi.study.data.excel.ExcelBuilder;
import easezhi.study.io.http.SpringRest;
import lombok.Data;
import org.junit.Test;
import study.cnbm.bean.Material;
import study.cnbm.sap.ViewQueryParam;
import study.cnbm.sap.ViewQueryResult;

import java.io.FileOutputStream;

public class MaterialTest {
    @Test
    public void exportTest() throws Exception {
        var fieldsJson = """
            {"FIELDS":
            [{"FIELDNAME":"code_ab","FIELDTYPE":"MATNR"},{"FIELDNAME":"MAKTX","FIELDTYPE":"MAKTX"},{"FIELDNAME":"SPART","FIELDTYPE":"SPART"},{"FIELDNAME":"VERPR","FIELDTYPE":"VERPR"},{"FIELDNAME":"BISMT","FIELDTYPE":"BISMT"},{"FIELDNAME":"ZZGKXH","FIELDTYPE":"ZGKXH"},{"FIELDNAME":"ZZMWSKZ","FIELDTYPE":"MWSKZ"},{"FIELDNAME":"LAEDA","FIELDTYPE":"LAEDA"},{"FIELDNAME":"AENAM","FIELDTYPE":"AENAM"},{"FIELDNAME":"ERSDA","FIELDTYPE":"ERSDA"},{"FIELDNAME":"ERNAM","FIELDTYPE":"ERNAM"},{"FIELDNAME":"MTART","FIELDTYPE":"MTART"},{"FIELDNAME":"ZZCPMC","FIELDTYPE":"ZCPMC"},{"FIELDNAME":"ZZMAKTX","FIELDTYPE":"ZMAKTX"},{"FIELDNAME":"MATKL","FIELDTYPE":"MATKL"},{"FIELDNAME":"MEINS","FIELDTYPE":"MEINS"},{"FIELDNAME":"MSEHL","FIELDTYPE":"MSEHL"},{"FIELDNAME":"MSTAE","FIELDTYPE":"MSTAE"},{"FIELDNAME":"ZVERSIONID","FIELDTYPE":"ZEVERSIONID"},{"FIELDNAME":"PRCTR","FIELDTYPE":"PRCTR"},{"FIELDNAME":"ZZKPMS","FIELDTYPE":"ZKPMS"},{"FIELDNAME":"WEBAZ","FIELDTYPE":"WEBAZ"},{"FIELDNAME":"WERKS","FIELDTYPE":"WERKS_D"},{"FIELDNAME":"PSTAT","FIELDTYPE":"PSTAT_D"},{"FIELDNAME":"MMSTA","FIELDTYPE":"MMSTA"}]
            }
            """;
        var param = JSON.parseObject(fieldsJson, ViewQueryParam.class);
        var sql = """
            select mara.MATNR,mara.MAKTX,mara.SPART,mara.VERPR,mara.BISMT,mara.ZZGKXH,mara.ZZMWSKZ,mara.LAEDA,mara.AENAM,mara.ERSDA,mara.ERNAM,mara.MTART,mara.ZZCPMC,mara.ZZMAKTX,mara.MATKL,mara.MEINS,mara.MSEHL,mara.MSTAE,mara.ZVERSIONID,mara.PRCTR,mara.ZZKPMS,mara.WEBAZ,marc.WERKS,marc.PSTAT,marc.MMSTA  from ZVMM_MARA mara left join ZVMM_MARC marc on mara.MANDT=marc.MANDT and mara.MATNR=marc.MATNR and charindex('E', marc.PSTAT) > -1  where mara.MANDT='560'  and mara.MSTAE not in ('01', '02') and marc.MMSTA not in ('01', '02') and marc.WERKS='1000' and mara.WERKS='1000' and mara.MATKL='product_line'""";
        var productLines = new String[]{"EC", "FL", "FR", "R1", "R2", "R3", "R4", "R5", "R6", "R7", "R8", "R9", "RJ"};
//        var productLines = new String[]{"EC"};
        var url = "http://it-sapet6.chinacloudapp.cn:8000/sap/api/query/common?sap-client=560";
        var outFile = "D:\\cnbm-work\\电商分销\\物料360-product_line.xlsx";
        var excelBuilder = ExcelBuilder.builder(Material.class);
        for (var pl: productLines) {
            param.setSql(sql.replace("product_line", pl));
            var rstStr = SpringRest.postJson(url, param);
            var rst = JSON.parseObject(rstStr, MatResult.class);
            var mats = rst.getResult();
            if (mats.isEmpty()) {
                System.out.println("产品线没有物料 " + pl);
                continue;
            }
            excelBuilder.build(new FileOutputStream(outFile.replace("product_line", pl)), mats);
        }
    }
}

class MatResult extends ViewQueryResult<Material> {
}
