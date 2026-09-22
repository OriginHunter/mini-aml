package com.miniaml.util;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class ListUtil {
    public static <T> boolean isEmpty(List<T> list) {
        return list == null || list.isEmpty();
    }

    public static <T> T getFirst(List<T> list) {
        if (list == null || list.isEmpty()) {
            return null;
        }
        return list.get(0);
    }

    public static <T, R> List<R> map(List<T> list, Function<T, R> mapper) {
        if (list == null || list.isEmpty()) {
            return new ArrayList<>();
        }
        List<R> listR = new ArrayList<>();
        for (T l : list) {
            listR.add(mapper.apply(l));
        }
        return listR;
    }
}
