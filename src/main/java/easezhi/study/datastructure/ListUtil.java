package easezhi.study.datastructure;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;

public class ListUtil {

    /**
     * 找出增删改。
     * 比较原数据列表(已经存在的数据)和目标数据列表(要保存的最终数据)，找出目标列表中要新增和更新的，原列表中要删除的。
     * @param sourceList 原数据列表
     * @param targetList 目标数据列表
     * @param keyGetter 从数据中获取能作为唯一标志值的函数(支持非唯一值)
     * @return 包含3个元素，分别是新增列表、更新列表和删除列表
     * @param <T> 数据的类型
     * @param <R> 作为唯一标志的值类型
     */
    public static <T, R> List<List<T>> pickCUD(List<T> sourceList, List<T> targetList, Function<T, R> keyGetter) {
        List<T> newList = new ArrayList<>();
        List<T> upList = new ArrayList<>();
        List<T> delList = new ArrayList<>();
        List<List<T>> rst = new ArrayList<>(3);
        rst.add(newList);
        rst.add(upList);
        rst.add(delList);
        if (sourceList.isEmpty()) {
            newList.addAll(targetList);
            return rst;
        }
        if (targetList.isEmpty()) {
            delList.addAll(sourceList);
            return rst;
        }

        Set<R> srcSet = new HashSet<>();
        Set<R> tarSet = new HashSet<>();
        for (T e: sourceList) {
            srcSet.add(keyGetter.apply(e));
        }
        for (T e: targetList) {
            R key = keyGetter.apply(e);
            tarSet.add(key);
            if (srcSet.contains(key)) {
                upList.add(e);
            } else {
                newList.add(e);
            }
        }
        for (T e: sourceList) {
            if (!tarSet.contains(keyGetter.apply(e))) {
                delList.add(e);
            }
        }

        return rst;
    }
}
