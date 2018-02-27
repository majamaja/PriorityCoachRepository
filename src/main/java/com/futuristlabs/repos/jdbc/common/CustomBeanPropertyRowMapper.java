package com.futuristlabs.repos.jdbc.common;

import org.postgresql.jdbc.PgArray;
import org.postgresql.util.PGInterval;
import org.springframework.jdbc.core.BeanPropertyRowMapper;

import java.beans.PropertyDescriptor;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Period;
import java.util.List;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;

public class CustomBeanPropertyRowMapper<T> extends BeanPropertyRowMapper<T> {
    public CustomBeanPropertyRowMapper(Class<T> mappedClass) {
        super(mappedClass);
    }

    public CustomBeanPropertyRowMapper(Class<T> mappedClass, boolean checkFullyPopulated) {
        super(mappedClass, checkFullyPopulated);
    }

    @Override
    protected Object getColumnValue(ResultSet rs, int index, PropertyDescriptor pd) throws SQLException {
        // @TODO: maybe replace this with a specialized converter one day ...
        Class<?> requiredType = pd.getPropertyType();

        if (Period.class.equals(requiredType)) {
            final PGInterval pg = (PGInterval) rs.getObject(index);
            return rs.wasNull() ? null : Period.of(pg.getYears(), pg.getMonths(), pg.getDays());
        } else if (List.class.equals(requiredType)) {
            final PgArray arr = (PgArray) rs.getArray(index);
            return rs.wasNull() ? emptyList() : asList((Object[]) arr.getArray());
        } else {
            return super.getColumnValue(rs, index, pd);
        }
    }
}
