package com.futuristlabs.repos.jdbc.common;

import lombok.AllArgsConstructor;
import org.springframework.jdbc.support.KeyHolder;

import java.util.UUID;

@AllArgsConstructor
public class InsertResult {
    private KeyHolder holder;
    private int updatedRowsCount;

    @SuppressWarnings("unchecked")
    public <T> T get(final String name) {
        return (T) holder.getKeys().get(name);
    }

    public UUID getId() {
        return get("id");
    }

    public int getUpdatedRowsCount() {
        return updatedRowsCount;
    }

    public boolean didInsertRows(final int expected) {
        return updatedRowsCount == expected;
    }

    public boolean didInsertRow() {
        return didInsertRows(1);
    }
}
