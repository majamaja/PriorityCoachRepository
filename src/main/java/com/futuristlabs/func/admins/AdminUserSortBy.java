package com.futuristlabs.func.admins;

import com.futuristlabs.utils.repository.SortBy;

import java.util.Arrays;
import java.util.List;

public enum AdminUserSortBy implements SortBy<AdminUser> {
    NAME("name"),
    USERNAME("username"),
    LAST_LOGIN("last_login");

    List<String> columns;

    AdminUserSortBy(String... columns) {
        this.columns = Arrays.asList(columns);
    }

    @Override
    public List<String> getColumns() {
        return columns;
    }
}
