package com.futuristlabs.p2p.repos.jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class SpringDB {

    @Autowired
    protected DataSource ds;

    // Mappers --------------------------------------------
    private static RowMapper<Long> longMapper() {
        return new RowMapper<Long>() {
            @Override
            public Long mapRow(ResultSet rs, int rowNum) throws SQLException {
                return rs.getLong(1);
            }
        };
    }

    private static RowMapper<String> stringMapper() {
        return new RowMapper<String>() {
            @Override
            public String mapRow(ResultSet rs, int rowNum) throws SQLException {
                return rs.getString(1);
            }
        };
    }

    private static RowMapper<Integer> intMapper() {
        return new RowMapper<Integer>() {
            @Override
            public Integer mapRow(ResultSet rs, int rowNum) throws SQLException {
                return rs.getInt(1);
            }
        };
    }

    private static RowMapper<Boolean> booleanMapper() {
        return new RowMapper<Boolean>() {
            @Override
            public Boolean mapRow(ResultSet rs, int rowNum) throws SQLException {
                return rs.getBoolean(1);
            }
        };
    }

    private static RowMapper<BigDecimal> bigDecimalMapper() {
        return new RowMapper<BigDecimal>() {
            @Override
            public BigDecimal mapRow(ResultSet rs, int rowNum) throws SQLException {
                return rs.getBigDecimal(1);
            }
        };
    }

    public Boolean returnBooleanSafe(String sql, SqlParameterSource params) {
        return returnSafe(sql, params, booleanMapper());
    }

    public Long returnLongSafe(String sql, SqlParameterSource params) {
        return returnSafe(sql, params, longMapper());
    }

    public String returnStringSafe(String sql, SqlParameterSource params) {
        return returnSafe(sql, params, stringMapper());
    }

    public BigDecimal returnBigDecimalSafe(String sql, SqlParameterSource params) {
        return returnSafe(sql, params, bigDecimalMapper());
    }

    public Integer returnIntegerSafe(String sql, SqlParameterSource params) {
        return returnSafe(sql, params, intMapper());
    }

    public boolean returnBooleanFromLong(String sql, SqlParameterSource params) {
        return returnLongSafe(sql, params) == 1l;
    }

    public <T> T returnSafe(String sql, SqlParameterSource params, RowMapper<T> mapper) {
        return returnSafe(new NamedParameterJdbcTemplate(ds), sql, params, mapper);
    }

    public int update(String sql, SqlParameterSource params) {
        return new NamedParameterJdbcTemplate(ds).update(sql, params);
    }

    public Map<String, Object> insert(String sql, SqlParameterSource params) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        new NamedParameterJdbcTemplate(ds).update(sql, params, keyHolder);
        return keyHolder.getKeys();
    }

    public long insertAndGet(String sql, String idColumn, SqlParameterSource params) {
        Map<String, Object> res = insert(sql, params);
        return ((Number) res.get(idColumn)).longValue();
    }

    public Long insertAndGetSafe(String sql, String idColumn, SqlParameterSource params) {
        Map<String, Object> res = insert(sql, params);
        if (res == null) {
            return null;
        }
        return ((Number) res.get(idColumn)).longValue();
    }

    public <T> List<T> returnList(String sql, SqlParameterSource params, RowMapper<T> mapper) {
        return new NamedParameterJdbcTemplate(ds).query(sql, params, mapper);
    }

    public <T> List<T> returnList(String sql, RowMapper<T> mapper) {
        return returnList(sql, new MapSqlParameterSource(), mapper);
    }

    public <K, V> Map<K, List<V>> returnMap(String sql, SqlParameterSource params, RowMapper<Pair<K, V>> mapper) {
        final List<Pair<K, V>> list = new NamedParameterJdbcTemplate(ds).query(sql, params, mapper);

        final Map<K, List<V>> result = new HashMap<>();
        for (Pair<K, V> pair : list) {
            if (!result.containsKey(pair.x)) {
                result.put(pair.x, new ArrayList<V>());
            }
            result.get(pair.x).add(pair.y);
        }
        return result;
    }

    private <T> T returnSafe(NamedParameterJdbcTemplate query, String sql, SqlParameterSource params, RowMapper<T> mapper) {
        final List<T> res = query.query(sql, params, mapper);

        if (res.size() != 1) {
            return null;
        }

        return res.get(0);
    }

    public static class Pair<K, V> {
        public K x;
        public V y;

        public Pair(K x, V y) {
            this.x = x;
            this.y = y;
        }
    }

}
