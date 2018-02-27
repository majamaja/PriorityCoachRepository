package com.futuristlabs.utils.excel;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Column<T, R> {
    private final String header;
    private final CellExtractor<T, R> extractor;
}
