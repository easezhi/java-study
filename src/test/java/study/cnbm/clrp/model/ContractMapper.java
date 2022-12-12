package study.cnbm.clrp.model;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface ContractMapper {
    ContractMapper INSTANCE = Mappers.getMapper(ContractMapper.class);

    PostLetter toPostLetter(ContractOrder contractOrder);

    List<PostLetter> toPostLetter(List<ContractOrder> contractOrderList);

    List<ReceiveLetter> toReceiveLetter(List<ContractOrder> contractOrderList);

    PurchaseOrder poFromExcel(PurchaseOrderExcel poExcel);

    List<PurchaseOrder> poFromExcel(List<PurchaseOrderExcel> list);

    PurchaseContract poToPurchaseContract(PurchaseOrder po);

    List<PurchaseContract> poToPurchaseContract(List<PurchaseOrder> poList);
}
