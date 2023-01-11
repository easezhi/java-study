package study.cnbm.clrp;

import easezhi.study.data.db.SqlBuilder;
import easezhi.study.data.excel.ExcelParser;
import easezhi.study.data.excel.ExcelUtil;
import easezhi.study.datastructure.CollectionUtil;
import easezhi.study.io.FileUtil;
import easezhi.study.lang.date.DateCalcu;
import org.junit.Test;
import study.cnbm.bean.User;
import study.cnbm.clrp.model.*;

import java.io.FileInputStream;
import java.time.LocalDateTime;
import java.util.*;

public class ClrpImport {
    int sqlBatch = 5000;
    String inDir = "D:\\cnbm-work\\文本收寄230111\\360原始单据\\";
    String outDir = "D:\\cnbm-work\\文本收寄230111\\360数据\\";
    String salesContractExcel = "销售合同.xlsx";
    String purchaseContractExcel = "采购合同.xlsx";
    String purchaseOrderExcel = "采购订单.xlsx";
    String supplierExcel = "供应商.XLSX";
    String rebateOffsetExcel = "返点冲抵.xlsx";
    String protocolExcel = "协议.xlsx";
    String userExcel = "员工账号.xlsx";

    LocalDateTime archiveTimeStart = LocalDateTime.of(2021,1,1,0,0);

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
        // 错误检查
        List<SalesContract> errList = scList.stream().filter(sc -> sc.getExcelBeanErrorMsg() != null).toList();
        if (!errList.isEmpty()) {
            errList.forEach(sc -> {
                System.out.println(ExcelUtil.formatExcelParseEntityError(sc));
            });
            return;
        }

        var userFile = inDir + userExcel;
        List<User> userList = ExcelParser.parser(User.class).parse(new FileInputStream(userFile));
        Map<String, String> userIdMap = CollectionUtil.toMap(userList, User::getKid, User::getLogin);

        scList.forEach(sc -> {
            sc.setSalesmanLogin(userIdMap.get(sc.getSalesmanLogin()));
        });

        // 设置最终版本
        var scMap = new HashMap<String, SalesContract>(scList.size());
        scList.forEach(sc -> {
            if (scMap.containsKey(sc.getContractGroup())) {
                var last = scMap.get(sc.getContractGroup());
                if (sc.getContractNo().compareTo(last.getContractNo()) > 0) {
                    scMap.put(sc.getContractGroup(), sc);
                    setSalesContractLast(sc);
                    setSalesContractNotLast(last);
                } else {
                    setSalesContractNotLast(sc);
                }
            } else {
                scMap.put(sc.getContractGroup(), sc);
                setSalesContractLast(sc);
            }
        });

        List<SalesContractCore> contractCoreList = scList.stream()
            .filter(sc -> sc.getApprovalStatus() == 2)
            .map(ContractMapper.INSTANCE::salesContractToCore)
            .toList();
        var sqlFile = outDir + "销售合同核心表.sql";
        var sql = SqlBuilder.builder(SalesContractCore.class, "public.sales_contract")
            .buildBatchInsertSql(contractCoreList, sqlBatch);
        FileUtil.writeStringToFile(sqlFile, sql.toString());
        System.out.printf("写入销售合同%d行\n", contractCoreList.size());

        List<ContractOrder> orderList = scList.stream()
            .filter(sc -> !(sc.getOrderStatus() == 2 && sc.getIsLast() == 0))
            .map(ContractMap::fromSalesContract)
            .toList();
        buildClrpSql(orderList, outDir, 1);
    }

    void setSalesContractLast(SalesContract sc) {
        sc.setIsLast(1);
        sc.setIsLastValid(sc.getOrderStatus() == 0 ? 0 : 1);
    }

    void setSalesContractNotLast(SalesContract sc) {
        sc.setIsLast(0);
        sc.setIsLastValid(0);
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

        // 设置最终版本
        var pcMap = new HashMap<String, PurchaseContract>(pcList.size());
        pcList.forEach(pc -> {
            if (pcMap.containsKey(pc.getContractGroup())) {
                var last = pcMap.get(pc.getContractGroup());
                if (pc.getContractNo().compareTo(last.getContractNo()) > 0) {
                    pcMap.put(pc.getContractGroup(), pc);
                    setPurchaseContractLast(pc);
                    setPurchaseContractNotLast(last);
                } else {
                    setPurchaseContractNotLast(pc);
                }
            } else {
                pcMap.put(pc.getContractGroup(), pc);
                setPurchaseContractLast(pc);
            }
        });

        // 采购合同核心表
        var sqlFile = outDir + "采购合同核心表-采购合同.sql";
        var sql = SqlBuilder.builder(PurchaseContract.class, "public.purchase_contract").buildBatchInsertSql(pcList, sqlBatch);
        FileUtil.writeStringToFile(sqlFile, sql.toString());
        System.out.printf("写入采购合同%d行\n", pcList.size());

        // 已作废的采购合同，即使多个版本，也只生成一条带后缀的，把作废状态非最新版的过滤掉
        List<ContractOrder> contractOrderList = new ArrayList<>(pcList.size() + 2000);
        pcList.forEach(pc -> {
            ContractOrder contractOrder = ContractMap.fromPurchaseContract(pc);
            contractOrderList.add(contractOrder);
            if (contractOrder.getIsRemove()) {
                contractOrder.setIsRemove(false);
                if (pc.getIsLast() == 1) {
                    contractOrderList.add(ContractMap.fromPurchaseContract(pc));
                }
            }
        });
        buildClrpSql(contractOrderList, outDir, 3);
    }

    void setPurchaseContractLast(PurchaseContract pc) {
        pc.setIsLast(1);
        pc.setIsLastValid(pc.getOrderStatus() == 0 ? 0 : 1);
    }

    void setPurchaseContractNotLast(PurchaseContract pc) {
        pc.setIsLast(0);
        pc.setIsLastValid(0);
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

        List<PurchaseOrder> poList = ContractMap.poExcelToPo(poRows);
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

        List<ContractOrder> contractOrderList = ContractMap.poToContractOrder(poList);
        buildClrpSql(contractOrderList, outDir, 4);
    }

    @Test
    public void importRebateSql() throws Exception {
        var roFile = inDir + rebateOffsetExcel;
        List<RebateOffset> roList = ExcelParser.parser(RebateOffset.class).parse(new FileInputStream(roFile));
        System.out.printf("共有返点冲抵%d条\n", roList.size());

        var userFile = inDir + userExcel;
        List<User> userList = ExcelParser.parser(User.class).parse(new FileInputStream(userFile));
        Map<String, String> userIdMap = CollectionUtil.toMap(userList, User::getKid, User::getLogin);

        roList.forEach(ro -> {
            ro.setCreateBy(userIdMap.get(ro.getCreateBy()));
        });

        List<ContractOrder> contractOrderList = new ArrayList<>(roList.size() + 200);
        roList.forEach(rebateOffset -> {
            ContractOrder contractOrder = ContractMap.fromRebateOffset(rebateOffset);
            contractOrderList.add(contractOrder);
            if (contractOrder.getIsRemove()) {
                contractOrder.setIsRemove(false);
                contractOrderList.add(ContractMap.fromRebateOffset(rebateOffset));
            }
        });
        buildClrpSql(contractOrderList, outDir, 5);
    }

    @Test
    public void importProtocolSql() throws Exception {
        var ptFile = inDir + protocolExcel;
        List<Protocol> protocolList = ExcelParser.parser(Protocol.class).parse(new FileInputStream(ptFile));
        System.out.printf("共有协议%d行\n", protocolList.size());

        // 230111 没填商务的历史单据，归档业务只要二期项目上线以后的
        var date = LocalDateTime.of(2023, 1, 6, 0, 0);
        protocolList = protocolList.stream().filter((protocol -> {
            return DateCalcu.timeGt(protocol.getEffectTime(), date) || DateCalcu.timeGt(protocol.getEditTime(), date);
        })).toList();

        // 检出已作废，要单独生成带后缀的
        List<ContractOrder> contractOrderList = new ArrayList<>(protocolList.size() + 200);
        Map<String, Protocol> protocolMap = new HashMap<>(); // 每个已作废协议的最新版本
        protocolList.forEach(protocol -> {
            ContractOrder contractOrder = ContractMap.fromProtocol(protocol);
            contractOrderList.add(contractOrder);
            if (!contractOrder.getIsRemove()) {
                return;
            }
            contractOrder.setIsRemove(false);
            if (protocolMap.containsKey(contractOrder.getOriginalContractNo())) {
                var last = protocolMap.get(contractOrder.getOriginalContractNo());
                if (contractOrder.getSalesContractNo().compareTo(last.getAgreementNo()) > 0) {
                    protocolMap.put(contractOrder.getOriginalContractNo(), protocol);
                }
            } else {
                protocolMap.put(contractOrder.getOriginalContractNo(), protocol);
            }
        });
        protocolMap.values().forEach(protocol -> contractOrderList.add(ContractMap.fromProtocol(protocol)));
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
//        contractOrderList = contractOrderList.stream().filter(e -> e.getContractBusinessMan() != null).toList();
        contractOrderList.forEach(contractOrder -> {
            if (contractOrder.getContractCustomerName() == null) {
                contractOrder.setContractCustomerName(""); // 客户null值影响收寄业务查询
            }
            // 已作废单据
            if (contractOrder.getIsRemove()) {
                contractOrder.setSalesContractNo(contractOrder.getOriginalContractNo() + "-JC");
            }
        });

        // 销售合同只处理近期数据
        if (source == 1) {
            contractOrderList = contractOrderList.stream().filter(this::contractToArchive).toList();
        }

        // 寄出表
        var pos = contractOrderList.stream();
        if (source == 1) {
            pos = pos.filter(order -> !order.getIsSendPulled());
        }
        List<PostLetter> plList = pos.map(ContractMapper.INSTANCE::toPostLetter).toList();
        if (!plList.isEmpty()) {
            var plSqlFile = dir + orderName + "导入寄出.sql";
            var plSql = SqlBuilder.builder(PostLetter.class, "public.dailyoffice_post_letter")
                .buildBatchInsertSql(plList, sqlBatch);
            FileUtil.writeStringToFile(plSqlFile, plSql.toString());
            System.out.printf("%s写入寄出%d行\n", orderName, plList.size());
        }

        // 收取表
        var ros = contractOrderList.stream();
        if (source == 1) {
            ros = ros.filter(order -> !order.getIsReceivePulled());
        }
        List<ReceiveLetter> rlList = ros.map(ContractMapper.INSTANCE::toReceiveLetter).toList();
        if (!rlList.isEmpty()) {
            var rlSqlFile = dir + orderName + "导入收取.sql";
            var rlSql = SqlBuilder.builder(ReceiveLetter.class, "public.dailyoffice_receive_letter")
                .buildBatchInsertSql(rlList, sqlBatch);
            FileUtil.writeStringToFile(rlSqlFile, rlSql.toString());
            System.out.printf("%s写入收取%d行\n", orderName, rlList.size());
        }

        // 归档表
        var aos = contractOrderList.stream();
        if (source == 1 || source == 2) {
            aos = aos.filter(order -> !order.getIsArchivePulled());
        }
        List<FileArchive> faList = aos.map(ContractMap::contractToArchive).toList();
        if (!faList.isEmpty()) {
            var arSqlFile = dir + orderName + "导入归档.sql";
            var arSql = SqlBuilder.builder(FileArchive.class, "public.dailyoffice_file_archived")
                .buildBatchInsertSql(faList, sqlBatch);
            FileUtil.writeStringToFile(arSqlFile, arSql.toString());
            System.out.printf("%s写入归档%d行\n", orderName, faList.size());
        }
    }

    boolean contractToArchive(ContractOrder order) {
        return order.getContractCreateTime() != null && order.getContractCreateTime().compareTo(archiveTimeStart) > 0;
    }
}
