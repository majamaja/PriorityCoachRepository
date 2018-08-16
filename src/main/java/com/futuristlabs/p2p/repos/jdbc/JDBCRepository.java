package com.futuristlabs.p2p.repos.jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public abstract class JDBCRepository {

    @Autowired
    protected SpringDB db;

    protected String onlyModified(String select) {
        return onlyModified(select, "");
    }

    protected String onlyModified(String select, String where) {
        return  select +
                " WHERE is_deleted = false " +
                " AND (:modifiedSince IS NULL OR last_modified > :modifiedSince) "
                + where;
    }

    protected String onlyModifiedForUser(String select) {
        return onlyModifiedForUser(select, "");
    }

    protected String onlyModifiedForUser(String select, String where) {
        return onlyModified(select, " AND user_id = :userId " + where);
    }

    protected String deletedSince(String table) {
        return deletedSince(table, "");
    }

    protected String deletedSince(String table, String where) {
        return  " SELECT id FROM " + table +
                " WHERE is_deleted = true " +
                " AND (:modifiedSince IS NULL OR last_modified > :modifiedSince)" +
                where;
    }

    protected String deletedSinceForUser(String table) {
        return deletedSinceForUser(table, "");
    }

    protected String deletedSinceForUser(String table, String where) {
        return deletedSince(table, " AND user_id = :userId " + where);
    }

    protected void deleteFromTable(String tableName, UUID userId, List<UUID> ids) {
        if (ids == null || ids.isEmpty()) {
            return;
        }

        final String sql = "UPDATE " + tableName + " SET is_deleted = true WHERE user_id = :userId AND id IN (:ids)";

        final MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("userId", userId.toString());
        params.addValue("ids", toStringList(ids));

        db.update(sql, params);
    }

    protected List<String> toStringList(final List<UUID> ids) {
        return ids.stream().map(UUID::toString).collect(Collectors.toList());
    }

}
