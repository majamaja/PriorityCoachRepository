package com.futuristlabs.utils;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SetUtils {
    public static <T> Set<T> setOf(T... values) {
        return new HashSet<>(Arrays.asList(values));
    }

    public static <T> Set<T> fromList(final List<? extends T> list) {
        return new HashSet<>(list);
    }

    private SetUtils() {
    }
}
