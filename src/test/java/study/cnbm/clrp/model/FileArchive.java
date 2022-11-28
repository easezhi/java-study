package study.cnbm.clrp.model;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class FileArchive {
    private String agreementNo;

    private String originalAgreementNo;

    private String source;

    private String signSubject;

    private String signCompany;

    private String relatedOrder;

    private String agreementName;

    private String agreementType;

    private BigDecimal rebatedContractAmt;

    private String businessMan;

    private String saleMan;

    private String agreementCreateBy;

    private String agreementCreateName;

    private String applyDept;

    private LocalDateTime agreementCreateTime;

    private LocalDateTime approvedTime;

    private String approvalStatus;
}
