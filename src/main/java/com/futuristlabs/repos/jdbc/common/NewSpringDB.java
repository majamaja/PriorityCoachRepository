package com.futuristlabs.repos.jdbc.common;

import com.futuristlabs.utils.Pair;
import com.futuristlabs.utils.repository.Page;
import com.futuristlabs.utils.repository.PageData;
import com.futuristlabs.utils.repository.SortBy;
import com.futuristlabs.utils.repository.SortOrder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.*;

@Component
public class NewSpringDB {
    private final Log log = LogFactory.getLog(getClass());

    private DataSource dataSource;
    protected NamedParameterJdbcTemplate template;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
        this.template = new NamedParameterJdbcTemplate(dataSource);
    }

    // SELECT
    private <T> RowMapper<T> beanMapper(Class<T> beanClass) {
        return new CustomBeanPropertyRowMapper<>(beanClass);
    }

    private <T, U> U checked(final RowMapper<T> mapper, final String sql, final Parameters params, Function<T, U> resultConverter) {
        T res = null;
        try {
            res = row(mapper, sql, params);
        } catch (IncorrectResultSizeDataAccessException e) {
            log.error(String.format("Incorrect result set size: expected %d, got %d\n", e.getExpectedSize(), e.getActualSize()));
            res = null;
        }
        return resultConverter.apply(res);
    }

    public <T> T safe(final RowMapper<T> mapper, final String sql, final Parameters params) {
        return checked(mapper, sql, params, Function.identity());
    }

    public <T> T safe(final RowMapper<T> mapper, final String sql, Parameter<?>... params) {
        return safe(mapper, sql, Parameters.fromList(params));
    }

    public <T> T safe(final Class<T> beanClass, final String sql, final Parameters params) {
        return safe(beanMapper(beanClass), sql, params);
    }

    public <T> T safe(final Class<T> beanClass, final String sql, Parameter<?>... params) {
        return safe(beanClass, sql, Parameters.fromList(params));
    }

    public <T> Optional<T> optional(final RowMapper<T> mapper, final String sql, final Parameters params) {
        return checked(mapper, sql, params, Optional::ofNullable);
    }

    public <T> Optional<T> optional(final RowMapper<T> mapper, final String sql, Parameter<?>... params) {
        return optional(mapper, sql, Parameters.fromList(params));
    }

    public <T> Optional<T> optional(final Class<T> beanClass, final String sql, final Parameters params) {
        return optional(beanMapper(beanClass), sql, params);
    }

    public <T> Optional<T> optional(final Class<T> beanClass, final String sql, Parameter<?>... params) {
        return optional(beanClass, sql, Parameters.fromList(params));
    }

    public <T> T row(final RowMapper<T> mapper, final String sql, final Parameters params) {
        return template.queryForObject(sql, params, mapper);
    }

    public <T> T row(final RowMapper<T> mapper, final String sql, Parameter<?>... params) {
        return row(mapper, sql, Parameters.fromList(params));
    }

    public <T> T getBean(final Class<T> beanClass, final String sql, final Parameters params) {
        return row(beanMapper(beanClass), sql, params);
    }

    public <T> T getBean(final Class<T> beanClass, final String sql, Parameter<?>... params) {
        return getBean(beanClass, sql, Parameters.fromList(params));
    }

    public <T> List<T> list(final RowMapper<T> mapper, final String sql, final Parameters params) {
        return template.query(sql, params, mapper);
    }

    public <T> List<T> list(final RowMapper<T> mapper, final String sql, Parameter<?>... params) {
        return list(mapper, sql, Parameters.fromList(params));
    }

    public <T> List<T> list(final Class<T> beanClass, final String sql, final Parameters params) {
        return list(beanMapper(beanClass), sql, params);
    }

    public <T> List<T> list(final Class<T> beanClass, final String sql, Parameter<?>... params) {
        return list(beanClass, sql, Parameters.fromList(params));
    }

    private <K, V> RowMapper<Pair<? extends K, ? extends V>> makePair(final RowMapper<? extends K> key, final RowMapper<? extends V> val) {
        return (rs, rowNum) -> new Pair<>(key.mapRow(rs, rowNum), val.mapRow(rs, rowNum));
    }

    public <K, V> Map<K, V> map(RowMapper<? extends K> key, RowMapper<? extends V> val, final String sql, final Parameters params) {
        return list(makePair(key, val), sql, params).stream()
                                                    .collect(toMap(Pair::getFirst, Pair::getSecond));
    }

    public <K, V> Map<K, V> map(RowMapper<? extends K> key, RowMapper<? extends V> val, final String sql, Parameter<?>... params) {
        return map(key, val, sql, Parameters.fromList(params));
    }

//    public <K, V> Map<K, List<V>> group(RowMapper<? extends K> key, RowMapper<? extends V> val, final String sql, final Parameters params) {
//        return list(makePair(key, val), sql, params).stream()
//                                                    .collect(groupingBy(Pair::getFirst, mapping(Pair::getSecond, toList())));
//    }

//    public <K, V> Map<K, List<V>> group(RowMapper<? extends K> key, RowMapper<? extends V> val, final String sql, Parameter<?>... params) {
//        return group(key, val, sql, Parameters.fromList(params));
//    }

    // paginated
    public <T> PageData<T> getPage(Class<T> beanClass, final String sql, final Page page, final SortBy<T> sortBy, final SortOrder sortOrder, final Parameters params) {
        final String countSql = String.format("SELECT count(*) FROM (%s) AS count", sql);
        final long itemsCount = row((rs, rowNum) -> rs.getLong(1), countSql, params);

        final String orderBy = sortBy.getColumns().stream().map(s -> s + " " + sortOrder).collect(Collectors.joining(", "));
        final String itemsSql = String.format("%s ORDER BY %s OFFSET :offset LIMIT :limit", sql, orderBy);

        params.set("offset", page.getPageOffset()).set("limit", page.getPageSize());
        final List<T> items = list(beanClass, itemsSql, params);

        return new PageData<>(items, itemsCount, page, sortBy, sortOrder);
    }

    public <T> PageData<T> getPage(Class<T> beanClass, final String sql, final Page page, final SortBy<T> sortBy, final SortOrder sortOrder, Parameter<?>... params) {
        return getPage(beanClass, sql, page, sortBy, sortOrder, Parameters.fromList(params));
    }

    // INSERT
    public InsertResult insert(String sql, Parameters params) {
        final KeyHolder keyHolder = new GeneratedKeyHolder();
        final int updatedRowsCount = template.update(sql, params, keyHolder);
        return new InsertResult(keyHolder, updatedRowsCount);
    }

    public InsertResult insert(String sql, Parameter<?>... params) {
        return insert(sql, Parameters.fromList(params));
    }

    public <T> InsertResult insert(String sql, T bean, Parameter<?>... params) {
        return insert(sql, new Parameters(bean).setAll(params));
    }

    // UPDATE (@TODO: add UpdateResult class?)
    public int update(final String sql, final Parameters params) {
        return template.update(sql, params);
    }

    public int update(String sql, Parameter<?>... params) {
        return update(sql, Parameters.fromList(params));
    }

    public <T> int update(String sql, T bean, Parameter<?>... params) {
        return update(sql, new Parameters(bean).setAll(params));
    }
}
