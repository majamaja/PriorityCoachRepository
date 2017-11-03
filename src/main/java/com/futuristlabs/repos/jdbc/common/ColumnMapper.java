package com.futuristlabs.repos.jdbc.common;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

@Data
@AllArgsConstructor
public class ColumnMapper<T> implements RowMapper<T> {
    private final String columnName;
    private final @Getter(AccessLevel.NONE) RowMapper<T> baseMapper;

    public RowMapper<T> safe() {
        return new CheckedMapper<>(this, Function.identity());
    }

    public RowMapper<Optional<T>> optional() {
        return new CheckedMapper<>(this, Optional::ofNullable);
    }

    public RowMapper<T> orElse(final Supplier<T> defVal) {
        return new CheckedMapper<>(this, _unused_ -> defVal.get());
    }

    @Override
    public T mapRow(ResultSet rs, int rowNum) throws SQLException {
        return baseMapper.mapRow(rs, rowNum);
    }

    @AllArgsConstructor
    private static class CheckedMapper<Raw, T> implements RowMapper<T> {
        private final ColumnMapper<Raw> extractor;
        private final Function<Raw, T> converter;

        @Override
        public T mapRow(ResultSet rs, int rowNum) throws SQLException {
            Raw val = null;
            if (hasColumn(rs, extractor.getColumnName())) {
                val = extractor.mapRow(rs, rowNum);
                if (rs.wasNull()) {
                    val = null;
                }
            }
            return converter.apply(val);
        }

        private boolean hasColumn(ResultSet rs, String columnName) throws SQLException {
            ResultSetMetaData meta = rs.getMetaData();
            final int columns = meta.getColumnCount();
            for (int i = 1; i <= columns; i++) {
                if (columnName.equals(meta.getColumnLabel(i))) {
                    return true;
                }
            }
            return false;
        }
    }
}
