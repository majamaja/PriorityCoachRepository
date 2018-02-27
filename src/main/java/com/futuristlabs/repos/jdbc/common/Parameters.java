package com.futuristlabs.repos.jdbc.common;

import org.postgresql.util.PGInterval;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

import java.time.Period;
import java.util.Arrays;
import java.util.Collection;

public class Parameters implements SqlParameterSource {
    private final BeanPropertySqlParameterSource beanSource;
    private final MapSqlParameterSource mapSource = new MapSqlParameterSource();

    public static Parameters fromList(Parameter<?>... parameters) {
        return new Parameters().setAll(parameters);
    }

    public Parameters(Object bean) {
        this.beanSource = new BeanPropertySqlParameterSource(bean);
    }

    public Parameters() {
        this(new Object());
    }

    public Parameters set(String name, Object value) {
        mapSource.addValue(name, value);
        return this;
    }

    public Parameters setAll(Parameter<?>... parameters) {
        Arrays.stream(parameters).forEach(param -> set(param.getParam(), param.getValue()));
        return this;
    }

    @Override
    public boolean hasValue(String paramName) {
        return mapSource.hasValue(paramName) || beanSource.hasValue(paramName);
    }

    @Override
    public Object getValue(String paramName) throws IllegalArgumentException {
        final Object value = mapSource.hasValue(paramName) ? mapSource.getValue(paramName) : beanSource.getValue(paramName);
        if (value instanceof Enum) {
            return value.toString();
        } else if (value instanceof Period) {
            final Period period = (Period) value;
            return new PGInterval(period.getYears(), period.getMonths(), period.getDays(), 0, 0, 0);
        } else if (value instanceof Collection) {
            return ((Collection) value).isEmpty() ? null : value;
        } else {
            return value;
        }
    }

    @Override
    public int getSqlType(String paramName) {
        return mapSource.hasValue(paramName) ? mapSource.getSqlType(paramName) : beanSource.getSqlType(paramName);
    }

    @Override
    public String getTypeName(String paramName) {
        return mapSource.hasValue(paramName) ? mapSource.getTypeName(paramName) : beanSource.getTypeName(paramName);
    }
}
