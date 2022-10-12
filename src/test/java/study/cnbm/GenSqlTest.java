package study.cnbm;

import org.junit.Test;
import study.cnbm.dict.DictDetail;

import java.util.ArrayList;

public class GenSqlTest {
    @Test
    public void testDictSql() {
        String dictName = "";
        String dictDesc = "";
        var details = new ArrayList<DictDetail>();
        details.add(new DictDetail("", ""));

        var dictIdSql = """
            (SELECT dict_id FROM public.sys_dict WHERE "name"='dictName')""".replaceAll("dictName", dictName);
        var dictSql = """
            -- dictDesc字典
            DELETE FROM public.sys_dict_detail WHERE dict_id = dictIdSql;
            DELETE FROM public.sys_dict WHERE "name" = 'dictName';

            INSERT INTO public.sys_dict ("name", description, create_by, update_by, create_time, update_time, is_del) VALUES
              ('dictName', 'dictDesc', 'admin_yw', 'admin_yw', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false);
            INSERT INTO public.sys_dict_detail (dict_id, "label", value, dict_sort, create_by, update_by, create_time, update_time, is_del, enabled) VALUES"""
            .replaceAll("dictIdSql", dictIdSql)
            .replaceAll("dictName", dictName)
            .replaceAll("dictDesc", dictDesc);
        System.out.println(dictSql);
        int total = details.size();
        for (var i = 0; i < total; i++) {
            var detail = details.get(i);
            int sort = i + 1;
            var detailSql = """
                (dictIdSql, 'detailLabel', 'detailValue', detailSort, 'admin_yw', 'admin_yw', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false, true)"""
                .replace("dictIdSql", dictIdSql)
                .replace("detailLabel", detail.getLabel())
                .replace("detailValue", detail.getValue())
                .replace("detailSort", String.valueOf(sort));
            System.out.println("  " + detailSql + (i < (total - 1) ? "," : ";"));
        }
    }
}
