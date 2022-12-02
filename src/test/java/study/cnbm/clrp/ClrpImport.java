package study.cnbm.clrp;

import easezhi.study.data.db.SqlBuilder;
import easezhi.study.data.excel.ExcelParser;
import easezhi.study.data.excel.annotation.ExcelColumn;
import easezhi.study.io.FileUtil;
import org.junit.Test;
import study.cnbm.clrp.model.*;

import java.io.FileInputStream;
import java.util.List;

public class ClrpImport {
    int sqlBatch = 5000;
    String inDir = "D:\\cnbm-work\\基石存储核心业务单据\\原始单据\\";
    String outDir = "D:\\cnbm-work\\基石存储核心业务单据\\360环境100条\\";

    @Test
    public void importSCSql() throws Exception {
        var scFile = inDir + "销售合同-360-100条.xlsx";
        List<SalesContract> scList = ExcelParser.parser(SalesContract.class).parse(new FileInputStream(scFile));

        var sqlFile = outDir + "销售合同核心表.sql";
        var sql = SqlBuilder.builder(SalesContract.class, "public.sales_contract")
            .buildBatchInsertSql(scList,sqlBatch);
        FileUtil.writeStringToFile(sqlFile, sql.toString());
        System.out.printf("写入销售合同%d行\n", scList.size());
    }

    @Test
    public void importPCSql() throws Exception {
        var pcFile = inDir + "采购合同-360-100条.xlsx";
        List<PurchaseContract> pcList = ExcelParser.parser(PurchaseContract.class).parse(new FileInputStream(pcFile));

        // 采购合同核心表
        var sqlFile = outDir + "采购合同核心表.sql";
        var sql = SqlBuilder.builder(PurchaseContract.class, "public.purchase_contract").buildBatchInsertSql(pcList, sqlBatch);
        FileUtil.writeStringToFile(sqlFile, sql.toString());
        System.out.printf("写入采购合同%d行\n", pcList.size());

        List<ContractOrder> contractOrderList = pcList.stream().map(ContractMap::fromPurchaseContract).toList();
        buildClrpSql(contractOrderList, outDir, 3);
    }

    @Test
    public void importPoSql() throws Exception {
        var poFile = inDir + "采购订单-360-100条.xlsx";
        List<PurchaseOrderExcel> poRows = ExcelParser.parser(PurchaseOrderExcel.class).parse((new FileInputStream(poFile)));
        List<PurchaseOrder> poList = ContractMapper.INSTANCE.poFromExcel(poRows);

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
        var roFile = inDir + "返点冲抵-360-100条.xlsx";
        List<RebateOffset> roList = ExcelParser.parser(RebateOffset.class).parse(new FileInputStream(roFile));
        List<ContractOrder> contractOrderList = roList.stream().map(ContractMap::fromRebateOffset).toList();
        buildClrpSql(contractOrderList, outDir, 5);
    }

    @Test
    public void importProtocolSql() throws Exception {
        var ptFile = inDir + "协议-360-100条.xlsx";
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

        // 寄出表
        List<PostLetter> plList = ContractMapper.INSTANCE.toPostLetter(contractOrderList);
        var plSqlFile = dir + orderName + "导入寄出.sql";
        var plSql = SqlBuilder.builder(PostLetter.class, "public.dailyoffice_post_letter")
            .buildBatchInsertSql(plList, sqlBatch);
        FileUtil.writeStringToFile(plSqlFile, plSql.toString());
        System.out.printf("%s写入寄出%d行\n", orderName, plList.size());

        // 收取表
        var rlSqlFile = dir + orderName + "导入收取.sql";
        var rlSql = SqlBuilder.builder(PostLetter.class, "public.dailyoffice_receive_letter")
            .buildBatchInsertSql(plList, sqlBatch);
        FileUtil.writeStringToFile(rlSqlFile, rlSql.toString());
        System.out.printf("%s写入收取%d行\n", orderName, plList.size());

        // 归档表
        List<FileArchive> faList = contractOrderList.stream()
            .map(ContractMap::contractToArchive).toList();
        var arSqlFile = dir + orderName + "导入归档.sql";
        var arSql = SqlBuilder.builder(FileArchive.class, "public.dailyoffice_file_archived")
            .buildBatchInsertSql(faList, sqlBatch);
        FileUtil.writeStringToFile(arSqlFile, arSql.toString());
        System.out.printf("%s写入归档%d行\n", orderName, plList.size());
    }
}
