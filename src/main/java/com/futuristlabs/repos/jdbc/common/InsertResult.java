package com.futuristlabs.repos.jdbc.common;

import lombok.AllArgsConstructor;
import org.springframework.jdbc.support.KeyHolder;

import java.util.UUID;

@AllArgsConstructor
public class InsertResult {
    private KeyHolder holder;

    @SuppressWarnings("unchecked")
    public <T> T get(final String name) {
        return (T)holder.getKeys().get(name);
    }

    public UUID getId() {
        return get("id");
    }
}
