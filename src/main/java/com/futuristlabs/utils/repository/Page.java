package com.futuristlabs.utils.repository;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.Min;

@Data
@AllArgsConstructor
public class Page {
    public static Page getAll() {
        return new Page(0, null);
    }

    private @Min(value = 1)
    int pageNumber;
    private @Min(value = 1)
    Integer pageSize;

    public Page() {
        this(1, 10);
    }

    public int getPageOffset() {
        return pageSize != null ? (pageNumber - 1) * pageSize : 0;
    }
}
