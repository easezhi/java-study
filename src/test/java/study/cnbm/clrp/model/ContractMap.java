package study.cnbm.clrp.model;

import study.cnbm.dict.JsonDict;

import java.util.ArrayList;
import java.util.List;

public class ContractMap {
    public static List<PurchaseOrder> poExcelToPo(List<PurchaseOrderExcel> poExcelList) {
        var poList = new ArrayList<PurchaseOrder>(poExcelList.size());
        poExcelList.forEach(poExcel -> {
            PurchaseOrder po = ContractMapper.INSTANCE.poFromExcel(poExcel);
            poList.add(po);
            if ("是".equals(po.getIsStandard())) {
                po.setIsStandard("1");
            } else if ("否".equals(po.getIsStandard())) {
                po.setIsStandard("2");
            }
        });
        return poList;
    }

    public static List<ContractOrder> poToContractOrder(List<PurchaseOrder> poList) {
        var contractOrders = new ArrayList<ContractOrder>(poList.size());
        poList.forEach(po -> {
            PurchaseContract purchaseContract = ContractMapper.INSTANCE.poToPurchaseContract(po);
            var orgMap = JsonDict.getPurchaseOrgMap();
            // 采购订单生成归档时，部门取采购组织
            var org = purchaseContract.getPurchaseOrg();
            if (org != null && orgMap.containsKey(org)) {
                purchaseContract.setCreatorOrg((String) orgMap.get(org));
            }
            ContractOrder contractOrder = fromPurchaseContract(purchaseContract);
            contractOrders.add(contractOrder);
        });
        return contractOrders;
    }

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

        if (contract.getOrderSrc() == 1) {
            // 合同编号
            contractOrder.setSalesContractNo(contract.getContractNo());
            // 合同原始版本号
            contractOrder.setOriginalContractNo(contract.getContractGroup());
            contractOrder.setSource("3");
        } else {
            contractOrder.setSalesContractNo(contract.getOrderNo());
            contractOrder.setOriginalContractNo(contract.getOrderNo());
            contractOrder.setSource("4");
        }
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

    public static ContractOrder fromRebateOffset(RebateOffset rebateOffset) {
        ContractOrder contractOrder = new ContractOrder();
        contractOrder.setSalesContractNo(rebateOffset.getCredenceNo());
        contractOrder.setOriginalContractNo(rebateOffset.getCredenceNo());
        contractOrder.setSource("5");
        // 关联单据，取关联的销售合同编号
        contractOrder.setRelatedOrder(rebateOffset.getSalesContractNo());
        contractOrder.setContractCustomerName(rebateOffset.getCustomer());
        contractOrder.setSignSubject(rebateOffset.getCorp());

        contractOrder.setRebatedContractAmt(rebateOffset.getContractRebatedAmt());

        contractOrder.setContractBusinessMan(rebateOffset.getBusinessMan());
        contractOrder.setContractSalesMan(rebateOffset.getSalesman());
        contractOrder.setContractCreateBy(rebateOffset.getCreateBy());
        contractOrder.setContractCreateName(rebateOffset.getCreatorName());
        contractOrder.setSalesApplyDept(rebateOffset.getCreatorOrg());
        contractOrder.setContractCreateTime(rebateOffset.getCreateTime());
        contractOrder.setApprovalStatus("2");
        return contractOrder;
    }

    public static ContractOrder fromProtocol(Protocol protocol) {
        ContractOrder contractOrder = new ContractOrder();
        contractOrder.setSalesContractNo(protocol.getAgreementNo());
        contractOrder.setOriginalContractNo(protocol.getAgreementGroup());
        contractOrder.setSource("2");
        // 签约主体，所属公司
        contractOrder.setSignSubject(protocol.getCorp());
        // 签约单位
        contractOrder.setContractCustomerName(protocol.getSignParty());
        // 关联单据编号，合作方协议号
        contractOrder.setRelatedOrder(protocol.getPartyAgreementNo());
        // 协议名称
        contractOrder.setEntryName(protocol.getAgreementName());
        contractOrder.setRebatedContractAmt(protocol.getAgreementAmt());
        // 协议类型
        contractOrder.setSalesContractType(protocol.getAgreementType());
        // 商务
        contractOrder.setContractBusinessMan(protocol.getBusinessMan());
        contractOrder.setContractSalesMan(protocol.getSalesman());
        contractOrder.setContractCreateName(protocol.getCreatorName());
        contractOrder.setContractCreateTime(protocol.getCreateTime());
        contractOrder.setSalesApplyDept(protocol.getCreatorOrg());
        contractOrder.setApprovalStatus("2");
        contractOrder.setApprovedTime(protocol.getEffectTime());
        return contractOrder;
    }
}
