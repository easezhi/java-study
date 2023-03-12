package study.cnbm.sap;

import com.alibaba.fastjson2.JSON;
import easezhi.study.io.http.HutoolHttp;
import easezhi.study.lang.SFunction;
import org.junit.Test;
import study.cnbm.sap.mapper.PoHeaderMapper;
import study.cnbm.sap.queryvo.PoQueryVo;
import study.cnbm.sap.view.PoHeaderView;
import study.cnbm.sap.viewquery.QueryUtil;
import study.cnbm.sap.viewquery.ViewQueryResult;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

public class PoTest {

    String url = "http://it-sapet5.chinacloudapp.cn:8000/sap/api/query/common?sap-client=550";

    public static class PoQueryResult extends ViewQueryResult<PoHeaderView> {}

    @Test
    public void testQueryPo() {
        var poViewMapper = new PoHeaderMapper();
        var poQuery = new PoQueryVo();
//        poQuery.setPoNo("4600000462");
        poQuery.setPoNos(Arrays.asList("4500183889", "4600000462"));
//        poQuery.setSupplierOrder("qwe");
        poQuery.setHasBusinessMan(true);
        poQuery.setOrderAmt(new BigDecimal("56.3"));
//        poQuery.setCompanyCode("1000");
        List<SFunction<PoHeaderView, Object>> select = Arrays.asList(PoHeaderView::getPoNo,
            PoHeaderView::getSupplierOrder, PoHeaderView::getOrderAmt);
//        var param = QueryUtil.buildSimpleQuery(poViewMapper, poQuery);
        var param = QueryUtil.buildQueryWithSelect(poViewMapper, select, poQuery);
        System.out.println(JSON.toJSONString(param));
        var rst = HutoolHttp.postJson(url, param, PoQueryResult.class);
        System.out.println(rst);
    }
}
