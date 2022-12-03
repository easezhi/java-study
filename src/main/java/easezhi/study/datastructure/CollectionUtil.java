package easezhi.study.datastructure;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

// 集合数据类型的工具函数
public class CollectionUtil {

    /**
     * 用于 java.util.stream.Collectors.toMap 的替代。
     * 原生函数必需提供合并函数，处理键重复的数据。本函数默认直接用后来的值替换先前的值
     * @param coll 数据集合
     * @param keyMapper 生成键的映射函数
     * @param valueMapper 生成值的映射函数
     * @return 键值映射
     * @param <T> 数据元素的类型
     * @param <K> 键类型
     * @param <U> 值类型
     */
    public static <T,K,U> Map<K, U> toMap(Collection<T> coll, Function<? super T, ? extends K> keyMapper,
                                          Function<T, U> valueMapper) {
        HashMap<K, U> rst = new HashMap<>(coll.size());
        coll.forEach(e -> rst.put(keyMapper.apply(e), valueMapper.apply(e)));
        return rst;
    }

    // Map 中的值是数据元素
    public static <T,K> Map<K, T> toMap(Collection<T> coll, Function<? super T, ? extends K> keyMapper) {
        HashMap<K, T> rst = new HashMap<>(coll.size());
        coll.forEach(e -> rst.put(keyMapper.apply(e), e));
        return rst;
    }
}

