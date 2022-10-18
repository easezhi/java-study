package study.cnbm;

import study.cnbm.dict.DictDetail;

import java.util.List;

public class CnbmUtil {

    public static String genDictSql(DictDetail dict, List<DictDetail> details) {
        var builder = new StringBuilder();
        var dictCode = dict.getValue();
        var dictDesc = dict.getLabel();
        var dictIdSql = """
            (SELECT dict_id FROM public.sys_dict WHERE "name"='dictCode')""".replaceAll("dictCode", dictCode);
        var dictSql = """
            -- dictDesc字典
            DELETE FROM public.sys_dict_detail WHERE dict_id = dictIdSql;
            DELETE FROM public.sys_dict WHERE "name" = 'dictCode';

            INSERT INTO public.sys_dict ("name", description, create_by, update_by, create_time, update_time, is_del) VALUES
              ('dictCode', 'dictDesc', 'admin_yw', 'admin_yw', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false);
            INSERT INTO public.sys_dict_detail (dict_id, "label", value, dict_sort, create_by, update_by, create_time, update_time, is_del, enabled) VALUES
            """
            .replaceAll("dictIdSql", dictIdSql)
            .replaceAll("dictCode", dictCode)
            .replaceAll("dictDesc", dictDesc);
        builder.append(dictSql);

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
            builder.append("  ")
                .append(detailSql)
                .append(i < (total - 1) ? ",\n" : ";");
        }
        return builder.toString();
    }
}
