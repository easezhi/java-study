package study.cnbm.clrp;

import easezhi.study.data.db.SqlBuilder;
import easezhi.study.data.excel.ExcelParser;
import easezhi.study.datastructure.CollectionUtil;
import easezhi.study.io.FileUtil;
import org.junit.Test;
import study.cnbm.bean.User;
import study.cnbm.clrp.model.*;

import java.io.FileInputStream;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ClrpImport {
    int sqlBatch = 5000;
    String inDir = "D:\\cnbm-work\\基石存储核心业务单据\\510原始单据\\";
    String outDir = "D:\\cnbm-work\\基石存储核心业务单据\\510数据\\";
    String salesContractExcel = "销售合同.xlsx";
    String purchaseContractExcel = "采购合同.xlsx";
    String purchaseOrderExcel = "采购订单.XLSX";
    String supplierExcel = "供应商.XLSX";
    String rebateOffsetExcel = "返点冲抵.xlsx";
    String protocolExcel = "协议.xlsx";
    String userExcel = "员工账号.xlsx";

    Set<String> getSuppliers() {
        var supplierSet = new HashSet<String>();
        supplierSet.add("1000008665");
        supplierSet.add("1000000670");
        supplierSet.add("1000008982");
        supplierSet.add("1000010676");
        supplierSet.add("1000008662");
        supplierSet.add("1000010225");
        supplierSet.add("1000011390");
        supplierSet.add("1000009737");
        supplierSet.add("1000009489");
        supplierSet.add("1000008621");
        supplierSet.add("1000010262");
        supplierSet.add("1000008815");
        supplierSet.add("1000008901");
        supplierSet.add("1000001686");
        supplierSet.add("1000007907");
        supplierSet.add("1000010200");
        supplierSet.add("1000008413");
        supplierSet.add("1000011019");
        supplierSet.add("1000010656");
        return supplierSet;
    }

    @Test
    public void importSalesContractSql() throws Exception {
        var scFile = inDir + salesContractExcel;
        List<SalesContract> scList = ExcelParser.parser(SalesContract.class).parse(new FileInputStream(scFile));

        var userFile = inDir + userExcel;
        List<User> userList = ExcelParser.parser(User.class).parse(new FileInputStream(userFile));
        Map<String, String> userIdMap = CollectionUtil.toMap(userList, User::getKid, User::getLogin);

        scList.forEach(sc -> {
            sc.setSalesmanLogin(userIdMap.get(sc.getSalesmanLogin()));
        });

        var sqlFile = outDir + "销售合同核心表.sql";
        var sql = SqlBuilder.builder(SalesContract.class, "public.sales_contract")
            .buildBatchInsertSql(scList,sqlBatch);
        FileUtil.writeStringToFile(sqlFile, sql.toString());
        System.out.printf("写入销售合同%d行\n", scList.size());
    }

    @Test
    public void importPurchaseContractSql() throws Exception {
        var pcFile = inDir + purchaseContractExcel;
        List<PurchaseContract> pcList = ExcelParser.parser(PurchaseContract.class).parse(new FileInputStream(pcFile));

        var userFile = inDir + userExcel;
        List<User> userList = ExcelParser.parser(User.class).parse(new FileInputStream(userFile));
        Map<String, String> userNameMap = CollectionUtil.toMap(userList, User::getName, User::getLogin);

        pcList.forEach(pc -> {
            pc.setBusinessManLogin(userNameMap.get(pc.getBusinessMan()));
            pc.setNeedArchive(getSuppliers().contains(pc.getSupplierId()) ? "1" : "0");
        });

        // 采购合同核心表
        var sqlFile = outDir + "采购合同核心表-采购合同.sql";
        var sql = SqlBuilder.builder(PurchaseContract.class, "public.purchase_contract").buildBatchInsertSql(pcList, sqlBatch);
        FileUtil.writeStringToFile(sqlFile, sql.toString());
        System.out.printf("写入采购合同%d行\n", pcList.size());

        List<ContractOrder> contractOrderList = pcList.stream().map(ContractMap::fromPurchaseContract).toList();
        buildClrpSql(contractOrderList, outDir, 3);
    }

    @Test
    public void importPurchaseOrderSql() throws Exception {
        var poFile = inDir + purchaseOrderExcel;
        List<PurchaseOrderExcel> poRows = ExcelParser.parser(PurchaseOrderExcel.class)
            .parse((new FileInputStream(poFile)));

        var userFile = inDir + userExcel;
        List<User> userList = ExcelParser.parser(User.class).parse(new FileInputStream(userFile));
        Map<String, String> userLoginMap = CollectionUtil.toMap(userList, User::getLogin, User::getName);
        Map<String, String> userNameMap = CollectionUtil.toMap(userList, User::getName, User::getLogin);

        var supplierFile = inDir + supplierExcel;
        List<PurchaseOrderSupplier> supplierList = ExcelParser.parser(PurchaseOrderSupplier.class)
            .parse(new FileInputStream(supplierFile));
        Map<String, String> supplierMap = CollectionUtil.toMap(supplierList,
            PurchaseOrderSupplier::getSupplierId, PurchaseOrderSupplier::getSupplier);

        List<PurchaseOrder> poList = ContractMapper.INSTANCE.poFromExcel(poRows);
        poList.forEach(po -> {
            po.setSupplier(supplierMap.get(po.getSupplierId()));
            po.setSalesmanLogin(userNameMap.get(po.getSalesman()));
            po.setBusinessManLogin(userNameMap.get(po.getBusinessMan()));
            var creatorName = po.getCreatorName(); // 值可能是账号或姓名
            if (creatorName != null) {
                if (userNameMap.containsKey(creatorName)) {
                    po.setCreateBy(userNameMap.get(creatorName));
                } else {
                    var createBy = creatorName.toLowerCase();
                    if (userLoginMap.containsKey(createBy)) {
                        po.setCreateBy(createBy);
                        po.setCreatorName(userLoginMap.get(createBy));
                    }
                }
            }
            po.setNeedArchive(getSuppliers().contains(po.getSupplierId()) ? "1" : "0");
        });

        var sqlFile = outDir + "采购合同核心表-采购订单.sql";
        var sql = SqlBuilder.builder(PurchaseOrder.class, "public.purchase_contract")
            .buildBatchInsertSql(poList, sqlBatch);
        FileUtil.writeStringToFile(sqlFile, sql.toString());
        System.out.printf("写入采购订单%d行\n", poList.size());

        List<PurchaseContract> pcList = ContractMapper.INSTANCE.poToPc(poList);
        List<ContractOrder> contractOrderList = pcList.stream().map(ContractMap::fromPurchaseContract).toList();
        buildClrpSql(contractOrderList, outDir, 4);
    }

    @Test
    public void importRebateSql() throws Exception {
        var roFile = inDir + rebateOffsetExcel;
        List<RebateOffset> roList = ExcelParser.parser(RebateOffset.class).parse(new FileInputStream(roFile));

        var userFile = inDir + userExcel;
        List<User> userList = ExcelParser.parser(User.class).parse(new FileInputStream(userFile));
        Map<String, String> userIdMap = CollectionUtil.toMap(userList, User::getKid, User::getLogin);

        roList.forEach(ro -> {
            ro.setCreateBy(userIdMap.get(ro.getCreateBy()));
        });

        List<ContractOrder> contractOrderList = roList.stream().map(ContractMap::fromRebateOffset).toList();
        buildClrpSql(contractOrderList, outDir, 5);
    }

    @Test
    public void importProtocolSql() throws Exception {
        var ptFile = inDir + protocolExcel;
        List<Protocol> protocolList = ExcelParser.parser(Protocol.class).parse(new FileInputStream(ptFile));
        List<ContractOrder> contractOrderList = protocolList.stream().map(ContractMap::fromProtocol).toList();
        buildClrpSql(contractOrderList, outDir, 2);
    }

    void buildClrpSql(List<ContractOrder> contractOrderList, String dir, int source) throws Exception {
        String orderName;
        if (source == 3) {
            orderName = "采购合同";
        } else if (source == 4) {
            orderName = "采购订单";
        } else if (source == 2) {
            orderName = "协议";
        } else if (source == 5) {
            orderName = "返点冲抵";
        } else {
            orderName = "销售合同";
        }

        // 过滤掉商务人员无值的
        contractOrderList = contractOrderList.stream().filter(e -> e.getContractBusinessMan() != null).toList();

        // 寄出表
        List<PostLetter> plList = ContractMapper.INSTANCE.toPostLetter(contractOrderList);
        var plSqlFile = dir + orderName + "导入寄出.sql";
        var plSql = SqlBuilder.builder(PostLetter.class, "public.dailyoffice_post_letter")
            .buildBatchInsertSql(plList, sqlBatch);
        FileUtil.writeStringToFile(plSqlFile, plSql.toString());
        System.out.printf("%s写入寄出%d行\n", orderName, plList.size());

        // 收取表
        List<ReceiveLetter> rlList = ContractMapper.INSTANCE.toReceiveLetter(contractOrderList);
        var rlSqlFile = dir + orderName + "导入收取.sql";
        var rlSql = SqlBuilder.builder(ReceiveLetter.class, "public.dailyoffice_receive_letter")
            .buildBatchInsertSql(rlList, sqlBatch);
        FileUtil.writeStringToFile(rlSqlFile, rlSql.toString());
        System.out.printf("%s写入收取%d行\n", orderName, rlList.size());

        // 归档表
        List<FileArchive> faList = contractOrderList.stream()
            .map(ContractMap::contractToArchive).toList();
        var arSqlFile = dir + orderName + "导入归档.sql";
        var arSql = SqlBuilder.builder(FileArchive.class, "public.dailyoffice_file_archived")
            .buildBatchInsertSql(faList, sqlBatch);
        FileUtil.writeStringToFile(arSqlFile, arSql.toString());
        System.out.printf("%s写入归档%d行\n", orderName, faList.size());
    }
}
