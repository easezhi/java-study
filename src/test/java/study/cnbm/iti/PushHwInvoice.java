package study.cnbm.iti;

import com.alibaba.fastjson2.JSON;
import easezhi.study.data.excel.ExcelParser;
import easezhi.study.io.http.HutoolHttp;
import org.junit.Test;
import study.cnbm.iti.model.HwInvoice;
import study.cnbm.iti.model.HwInvoiceItem;
import study.cnbm.iti.model.HwInvoicePushResult;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class PushHwInvoice {

    @Test
    public void pushInvoice() throws Exception {
        var file = "D:\\cnbm-work\\华为进项发票\\测试\\测试推送发票\\华为进项发票信息-造数据11.xlsx";
        List<HwInvoice> invoiceList = ExcelParser.parser(HwInvoice.class).parseExcelFile(file);
//        List<HwInvoiceItem> itemList = ExcelParser.parser(HwInvoiceItem.class).parseExcelFile(file);
//        var noItemList = new ArrayList<String>();
//        var itemGroup = itemList.stream().collect(Collectors.groupingBy(HwInvoiceItem::getInvoiceNumber));
//        invoiceList.forEach(invoice -> {
//            var items = itemGroup.get(invoice.getInvoiceNumber());
//            if (items == null) {
//                noItemList.add(invoice.getInvoiceNumber());
//            } else {
//                invoice.setItemList(items);
//            }
//        });
//        if (noItemList.isEmpty()) {
//            System.out.println("缺少行项目：" + String.join(",", noItemList));
//        }
        invoiceList.forEach(invoice -> {
            HwInvoiceItem item = new HwInvoiceItem();
//            item.setInvoiceNumber(invoice.getInvoiceNumber());
            item.setTaxRate(invoice.getTaxRate());
            invoice.setInvoiceItem(Collections.singletonList(item));
        });
        var url = "https://cdwp-test01.chinacloudapp.cn/openapi/hw/api/invoice";
        invoiceList.forEach(invoice -> {
//            System.out.println(JSON.toJSONString(invoice));
            var rst = HutoolHttp.postJson(url, invoice, HwInvoicePushResult.class);
            System.out.printf("推送发票：%s %s %s\n", invoice.getInvoiceNumber(), rst.getReturnStatus(), rst.getReturnDescription() );
        });
    }
}
