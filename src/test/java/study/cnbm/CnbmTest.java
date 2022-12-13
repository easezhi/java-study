package study.cnbm;

import easezhi.study.data.db.SqlBuilder;
import org.junit.Test;
import study.cnbm.clrp.model.PostLetter;

import java.util.Collections;

public class CnbmTest {
    @Test
    public void test() throws Exception {
        var ss = """
            a'b''c""";
        var postLetter = new PostLetter();
        postLetter.setSalesContractNo(ss);
        var sql = SqlBuilder.builder(PostLetter.class, "order")
                .buildBatchInsertSql(Collections.singletonList(postLetter));
        System.out.println(sql);
    }
}
