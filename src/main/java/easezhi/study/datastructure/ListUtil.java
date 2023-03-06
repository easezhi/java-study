package easezhi.study.datastructure;

import java.util.*;
import java.util.function.BiConsumer;
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

    /**
     * 列表分组
     * @param beanList 待分组大列表
     * @param batch 每组数量
     * @return 分组后的小列表的列表
     * @param <T> 元素类型
     */
    public static <T> List<List<T>> partition(List<T> beanList, int batch) {
        var batchList = new ArrayList<List<T>>();
        int total = beanList.size();
        for (int i = 0; i < total; i += batch) {
            int j = i + batch;
            if (j > total) j = total;
            batchList.add(beanList.subList(i, j));
        }
        return batchList;
    }

    /**
     * 根据指定的键、值字段，把源单据中的值赋值到对应目标单据中
     * 目标单据与源单据键字段值相等即存在对应关系
     * @param tarList 待填充值的目标单据列表
     * @param tarKeyGetter 目标单据键字段getter
     * @param tarValueSetter 目标单据值字段setter
     * @param srcList 提供值的源单据列表
     * @param srcKeyGetter 源单据键字段getter
     * @param srcValueGetter 源单据值字段getter
     * @param <T> 目标单据类型
     * @param <S> 源单据类型
     * @param <K> 键字段的类型
     * @param <V> 值字段的类型
     */
    public static <T, S, K, V> void refillValue(List<T> tarList, Function<T, K> tarKeyGetter, BiConsumer<T, V> tarValueSetter,
                                                List<S> srcList, Function<S, K> srcKeyGetter, Function<S, V> srcValueGetter) {
        Map<K, V> valueMap = new HashMap<>();
        srcList.forEach(src -> valueMap.put(srcKeyGetter.apply(src), srcValueGetter.apply(src)));
        tarList.forEach(tar -> {
            K key = tarKeyGetter.apply(tar);
            if (valueMap.containsKey(key)) {
                tarValueSetter.accept(tar, valueMap.get(key));
            }
        });
    }
}
