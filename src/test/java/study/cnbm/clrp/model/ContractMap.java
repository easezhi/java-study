package study.cnbm.clrp.model;

public class ContractMap {

    public static FileArchive contractToArchive(ContractOrder contractOrder) {
        FileArchive fileArchive = new FileArchive();
        fileArchive.setAgreementNo(contractOrder.getSalesContractNo());
        fileArchive.setOriginalAgreementNo(contractOrder.getOriginalContractNo());
        fileArchive.setSource(contractOrder.getSource());

        fileArchive.setSignSubject(contractOrder.getSignSubject());
        fileArchive.setSignCompany(contractOrder.getContractCustomerName());
        fileArchive.setRelatedOrder(contractOrder.getRelatedOrder());

        fileArchive.setAgreementName(contractOrder.getEntryName());
        fileArchive.setAgreementType(contractOrder.getSalesContractType());
        fileArchive.setRebatedContractAmt(contractOrder.getRebatedContractAmt());

        fileArchive.setBusinessMan(contractOrder.getContractBusinessMan());
        fileArchive.setSaleMan(contractOrder.getContractSalesMan());
        fileArchive.setAgreementCreateBy(contractOrder.getContractCreateBy());
        fileArchive.setAgreementCreateName(contractOrder.getContractCreateName());
        fileArchive.setApplyDept(contractOrder.getSalesApplyDept());
        fileArchive.setAgreementCreateTime(contractOrder.getContractCreateTime());
        fileArchive.setApprovedTime(contractOrder.getApprovedTime());
        fileArchive.setApprovalStatus(contractOrder.getApprovalStatus());
        return fileArchive;
    }

    public static ContractOrder fromPurchaseContract(PurchaseContract contract) {
        ContractOrder contractOrder = new ContractOrder();

        // 合同编号
        contractOrder.setSalesContractNo(contract.getContractNo());
        // 合同原始版本号
        contractOrder.setOriginalContractNo(contract.getContractGroup());
        contractOrder.setSource("3");
        // 关联业务编号，供应商订单号
        contractOrder.setRelatedOrder(contract.getSupplierOrder());
        // 签约主体编号
        contractOrder.setSignSubject(contract.getCorp());
        // 签约单位，供应商
        contractOrder.setContractCustomerName(contract.getSupplier());
        // 项目名称
        contractOrder.setEntryName(contract.getProjectName());
        // 合同金额
        contractOrder.setRebatedContractAmt(contract.getExRebateAmt());
        // 商务人员姓名
        contractOrder.setContractBusinessMan(contract.getBusinessMan());
        // 销售人员姓名
        contractOrder.setContractSalesMan(contract.getSalesman());
        // 申请部门。采购合同创建人的部门，而不是销售部门
        contractOrder.setSalesApplyDept(contract.getCreatorOrg());
        // 创建人姓名
        contractOrder.setContractCreateName(contract.getCreatorName());
        contractOrder.setContractCreateBy(contract.getCreateBy());
        // 创建时间
        contractOrder.setContractCreateTime(contract.getCreateTime());
        contractOrder.setIsNeedArchived(contract.getNeedArchive());
        contractOrder.setApprovalStatus(contract.getApprovalStatus().toString());
        // 采购合同类型
        contractOrder.setSalesContractType(contract.getOrderType());
        return contractOrder;
    }
}
