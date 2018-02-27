package com.futuristlabs.utils.excel;

public interface CellExtractor<T, R> {
    R extractValue(final T t);
}
