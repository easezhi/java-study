package study.cnbm.iti;

import easezhi.study.data.db.SqlBuilder;
import easezhi.study.data.excel.ExcelParser;
import easezhi.study.io.FileUtil;
import org.junit.Test;
import study.cnbm.dict.CommonDict;
import study.cnbm.iti.model.Invoice;

import java.util.HashMap;
import java.util.List;

public class ImportInvoice {
    String dir = "D:\\cnbm-work\\华为进项发票\\发票数据\\历史发票-开发\\";

    String invoiceFile = "发票.xlsx";

    String itemFile = "";

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
}
