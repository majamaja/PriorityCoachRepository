package com.futuristlabs.p2p.repos;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.UUID;

@Repository
public class SampleData {

    @Autowired
    private DataSource ds;

    private boolean log = false;

    public void enableLog() {
        this.log = true;
    }

    public UUID nativeAuth(UUID id, String email) {
        insert("INSERT INTO native_users (id, email, password) VALUES (:1, :2, :3)", id, email, UUID.randomUUID());
        return id;
    }

    public UUID user() {
        final UUID userId = UUID.randomUUID();
        final String email = userId + "@example.com";

        insert("INSERT INTO users (id, name, email, native_user_id) VALUES (:1, :2, :3, :4)", userId, "Sample User", email, nativeAuth(userId, email));
        return userId;
    }

    public UUID friendship(UUID userId, UUID friend) {
        UUID friendshipId = UUID.randomUUID();
        insert("INSERT INTO user_friends (id, user_id, friend_id) VALUES (:1, :2, :3)", friendshipId, userId, friend);
        return friendshipId;
    }

    public UUID lifeUpgradeCategory() {
        UUID categoryId = UUID.randomUUID();
        insert("INSERT INTO life_upgrade_categories (id, name) VALUES (:1, :2)", categoryId, "Upgrade Categoty " + categoryId);
        return categoryId;
    }

    public UUID lifeUpgradeAction() {
        return lifeUpgradeAction(user(), lifeUpgradeCategory());
    }

    public UUID lifeUpgradeActionWithUser(UUID userId) {
        return lifeUpgradeAction(userId, lifeUpgradeCategory());
    }

    public UUID lifeUpgradeAction(UUID userId, UUID lifeUpgradeCategoryId) {
        UUID actionId = UUID.randomUUID();
        insert("INSERT INTO life_upgrade_actions (id, life_upgrade_category_id, name, times_per_week, user_id) VALUES (:1, :2, :3, :4, :5)", actionId, lifeUpgradeCategoryId, "Upgrade Action " + actionId, 3, userId);
        return actionId;
    }

    public UUID permissions(final UUID userId, final UUID friendshipId, final UUID actionId, final boolean visible) {
        UUID permissionId = UUID.randomUUID();
        insert("INSERT INTO user_friend_permissions (id, user_id, friendship_id, life_upgrade_action_id, visible) VALUES (:1, :2, :3, :4, :5)", permissionId, userId, friendshipId, actionId, visible);
        return permissionId;
    }

    public UUID actionLog(final UUID userId, final UUID actionId) {
        UUID actionLogId = UUID.randomUUID();
        insert("INSERT INTO user_actions_log (id, user_id, life_upgrade_action_id) VALUES (:1, :2, :3)", actionLogId, userId, actionId);
        return actionLogId;
    }

    private void delete(String sql, Object... ids) {
        execute(sql, ids);
    }

    private void insert(String sql, Object... ids) {
        execute(sql, ids);
    }

    private void execute(String sql, Object... ids) {
        log(sql, ids);

        MapSqlParameterSource params = new MapSqlParameterSource();
        for (int i = 1; i <= ids.length; i++) {
            Object param = null;
            if (ids[i - 1] instanceof UUID) {
                param = ids[i - 1].toString();
            } else {
                param = ids[i - 1];
            }
            params.addValue(String.valueOf(i), param);
        }

        NamedParameterJdbcTemplate query = new NamedParameterJdbcTemplate(ds);
        query.update(sql, params);
    }

    public void list(String table) {
        MapSqlParameterSource params = new MapSqlParameterSource();

        NamedParameterJdbcTemplate query = new NamedParameterJdbcTemplate(ds);
        final String sql = " SELECT * " + " FROM " + table;

        System.out.println("Listing:____________________" + table);
        query.query(sql, params, new RowMapper<Object>() {
            @Override
            public Object mapRow(ResultSet rs, int idx) throws SQLException {
                for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
                    System.out.print("  " + rs.getMetaData().getColumnName(i) + ": " + rs.getObject(i) + "\t");
                }
                System.out.println();
                return null;
            }
        });
        System.out.println("END\n");
    }

    private void log(String sql, Object[] ids) {
        if (log) {
            System.out.println("SQL: " + sql + " ::: " + Arrays.asList(ids));
        }
    }
}
