package com.futuristlabs.utils.repository;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.*;

@Data
@NoArgsConstructor
public abstract class Sort<T> {
    public static class Config {
        private final Map<String, List<String>> config = new HashMap<>();

        public Config set(final String key, String ... columns) {
            config.put(key, Collections.unmodifiableList(Arrays.asList(columns)));
            return this;
        }

        public boolean hasKey(final String key) {
            return config.containsKey(key);
        }

        public List<String> getKey(final String key) {
            return config.get(key);
        }
    }

    private @NotNull String sortBy;
    private @NotNull SortOrder sortOrder;

    public abstract Config getConfig();

    public Sort(String sortBy, SortOrder sortOrder) {
        setSortBy(sortBy);
        setSortOrder(sortOrder);
    }

    public void setSortBy(String sortBy) {
        if (!getConfig().hasKey(sortBy)) {
            throw new IllegalArgumentException("Column <" + sortBy + "> is not allowed for sorting");
        }
        this.sortBy = sortBy;
    }
}
