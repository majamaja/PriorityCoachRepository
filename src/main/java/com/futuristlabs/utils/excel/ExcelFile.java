package com.futuristlabs.utils.excel;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ExcelFile {
    public static final String MIME_TYPE = "application/vnd.ms-excel";

    private final byte[] bytes;
}
