package com.futuristlabs.utils.repository;

import lombok.Data;

import java.util.List;

@Data
public class PageData<T> {
    private final List<T> items;
    private final Long totalItems;
    private final Integer pageNumber;
    private final Integer pageSize;
    private final SortBy<T> sortBy;
    private final SortOrder sortOrder;

    public PageData(List<T> items, Long totalItems, Page page, SortBy<T> sortBy, SortOrder sortOrder) {
        assert page == null || page.getPageSize() == null || items.size() <= page.getPageSize();
        assert page == null || page.getPageOffset() + items.size() <= totalItems;

        this.items = items;
        this.totalItems = totalItems;
        this.pageNumber = page != null ? page.getPageNumber() : null;
        this.pageSize = page != null ? page.getPageSize() : null;
        this.sortBy = sortBy;
        this.sortOrder = sortOrder;
    }

    public PageData(final List<T> items) {
        this(items, null, null, null, null);
    }

    public PageData(List<T> items, Long totalItems, Page page) {
        this(items, totalItems, page, null, null);
    }
}
