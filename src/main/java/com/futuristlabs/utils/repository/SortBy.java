package com.futuristlabs.utils.repository;

import java.util.List;

public interface SortBy<T> {
    List<String> getColumns();
}
