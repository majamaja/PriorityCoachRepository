package com.futuristlabs.utils.repository;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.Min;

@Data
@AllArgsConstructor
public class Page {
    private @Min(value = 1) int pageNumber;
    private @Min(value = 1) int pageSize;

    public Page() {
        this(1, 1);
    }

    public int getPageOffset() {
        return (pageNumber - 1) * pageSize;
    }
}
