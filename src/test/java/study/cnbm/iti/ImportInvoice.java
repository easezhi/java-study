package study.cnbm.iti;

import easezhi.study.data.db.SqlBuilder;
import easezhi.study.data.excel.ExcelParser;
import easezhi.study.io.FileUtil;
import org.junit.Test;
import study.cnbm.dict.CommonDict;
import study.cnbm.iti.model.Invoice;
import study.cnbm.iti.model.PurchaseOrder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class ImportInvoice {
    String dir = "D:\\cnbm-work\\华为进项发票\\发票数据\\历史发票-开发\\";

    String invoiceFile = "发票.xlsx";

    String poFile = "采购订单.xlsx";

    int sqlBatch = 5000;

    @Test
    public void importInvoice() throws Exception {
        // 公司编号
        var companyMap = new HashMap<String, String>();
        var invoiceFinanceType = HWDict.invoiceFinanceType;
        var taxRateCode = HWDict.taxRateCode;

        for (var ent: CommonDict.company.entrySet()) {
            companyMap.put(ent.getValue(), ent.getKey());
        }
        List<Invoice> invoiceList = ExcelParser.parser(Invoice.class).parseExcelFile(dir + invoiceFile);
        // 暂时过滤掉退票
        invoiceList = invoiceList.stream().filter(invoice -> !HWDict.returnInvoiceType.equals(invoice.getApplyType())).toList();

        invoiceList.forEach(invoice -> {
            invoice.setUpdateTime(invoice.getCreateTime());
            invoice.setCompanyCode(companyMap.getOrDefault(invoice.getCompanyName(), null));
            invoice.setInvoiceFinanceType(invoiceFinanceType.getOrDefault(invoice.getApplyType(), null));
            invoice.setTaxRate(taxRateCode.getOrDefault(invoice.getTaxRate(), null));
        });
        System.out.printf("共有发票%d行\n", invoiceList.size());

        String invoiceSqlFile = dir + "导入发票.sql";
        var invoiceSql = SqlBuilder.builder(Invoice.class, "public.iti_invoice").buildBatchInsertSql(invoiceList, sqlBatch);
        FileUtil.writeStringToFile(invoiceSqlFile, invoiceSql.toString());
        System.out.println("导入发票脚本完成");
    }

    // 往发票中补充采购订单号和商务
    @Test
    public void refillPo() throws Exception {
        List<Invoice> invoiceList = ExcelParser.parser(Invoice.class).parseExcelFile(dir + invoiceFile);
        List<PurchaseOrder> poList = ExcelParser.parser(PurchaseOrder.class).parseExcelFile(dir + poFile);
        var poSet = new HashSet<>();
        invoiceList.forEach(invoice -> poSet.add(invoice.getSupplierOrder()));
        var sqlTemp = "UPDATE iti_invoice SET po_no='poNo', business_name='businessName' WHERE supplier_order='supplierOrder';";
        var sqlList = new ArrayList<String>();
        poList.forEach(po -> {
            if (!poSet.contains(po.getSupplierOrder())) {
                return;
            }
            var sql = sqlTemp.replace("poNo", po.getPoNo())
                .replace("businessName", po.getBusinessMan() == null ? "" : po.getBusinessMan())
                .replace("supplierOrder", po.getSupplierOrder());
            sqlList.add(sql);
        });
        String outFile = dir + "补充采购订单.sql";
        FileUtil.writeLinesToFile(outFile, sqlList);
        System.out.printf("录入采购订单%d条", sqlList.size());
    }
}
