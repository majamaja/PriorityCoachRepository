package com.futuristlabs.func.users;

import com.futuristlabs.utils.repository.SortBy;

import java.util.Arrays;
import java.util.List;

public enum UserSortBy implements SortBy<User> {
    NAME("name"),
    EMAIL("email"),
    SIGNED_UP("created_at");

    List<String> columns;

    UserSortBy(String... columns) {
        this.columns = Arrays.asList(columns);
    }

    @Override
    public List<String> getColumns() {
        return columns;
    }
}
