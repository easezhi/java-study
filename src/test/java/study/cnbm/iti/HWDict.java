package study.cnbm.iti;

import java.util.HashMap;

public class HWDict {
    // 退票申请类型
    public static final String returnInvoiceType = "Return Blue Invoice";

    // 税率转编码
    public static HashMap<String, String> taxRateCode;

    // 蓝票、红票
    public static HashMap<String, String> invoiceFinanceType;

    static {
        taxRateCode = new HashMap<>();
        taxRateCode.put("0", "J0");
        taxRateCode.put("0.03", "J4");
        taxRateCode.put("0.06", "J3");
        taxRateCode.put("0.09", "J8");
        taxRateCode.put("0.1", "J6");
        taxRateCode.put("0.11", "J2");
        taxRateCode.put("0.13", "J7");
        taxRateCode.put("0.16", "J5");
        taxRateCode.put("0.17", "J1");

        invoiceFinanceType = new HashMap<>();
        invoiceFinanceType.put("Positive Invoice(With contract)", "1"); // 蓝票
        invoiceFinanceType.put("Negative Invoice", "2"); // 红票
    }
}
