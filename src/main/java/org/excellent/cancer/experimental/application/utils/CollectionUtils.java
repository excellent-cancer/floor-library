package org.excellent.cancer.experimental.application.utils;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * 集合帮助类
 *
 * @author XyParaCrim
 */
public final class CollectionUtils {

    /**
     * 将{@link Iterable<T>}类型转成{@link List<T>}
     *
     * @param elements 可迭代元素
     * @param <T> 元素类型
     * @return 返回一个List
     */
    public static <T> List<T> asList(Iterable<? extends T> elements) {
        return elements == null ?
                Collections.emptyList() :
                StreamSupport.stream(elements.spliterator(), false).collect(Collectors.toList());
    }


}
