package com.futuristlabs.repos.jdbc.common;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public abstract class AbstractJdbcRepository {
    protected SpringDB db;

    @Autowired
    public void setDb(SpringDB db) {
        this.db = db;
    }

    protected <T> Parameter<T> set(final String name, final T value) {
        return new Parameter<>(name, value);
    }

    @SuppressWarnings("unchecked")
    protected <T> RowMapper<T> get(final String name) {
        return new ColumnMapper<>(name, (rs, rowNum) -> (T)rs.getObject(name));
    }

    protected static ColumnMapper<Boolean> getBool(final String name) {
        return new ColumnMapper<>(name, (rs, rowNum) -> rs.getBoolean(name));
    }

    protected static ColumnMapper<Integer> getInt(final String name) {
        return new ColumnMapper<>(name, (rs, rowNum) -> rs.getInt(name));
    }

    protected static ColumnMapper<Long> getLong(final String name) {
        return new ColumnMapper<>(name, (rs, rowNum) -> rs.getLong(name));
    }

    protected static ColumnMapper<BigDecimal> getBigDecimal(final String name) {
        return new ColumnMapper<>(name, (rs, rowNum) -> rs.getBigDecimal(name));
    }

    protected static ColumnMapper<UUID> getUUID(final String name) {
        return new ColumnMapper<>(name, (rs, rowNum) -> (UUID) rs.getObject(name));
    }

    protected static ColumnMapper<String> getString(final String name) {
        return new ColumnMapper<>(name, (rs, rowNum) -> rs.getString(name));
    }

    protected static ColumnMapper<LocalDateTime> getDateTime(final String name) {
        return new ColumnMapper<>(name, (rs, rowNum) -> rs.getTimestamp(name).toLocalDateTime());
    }

    protected static <T> RowMapper<T> getBean(final Class<T> clazz) {
        return new CustomBeanPropertyRowMapper<>(clazz);
    }
}
