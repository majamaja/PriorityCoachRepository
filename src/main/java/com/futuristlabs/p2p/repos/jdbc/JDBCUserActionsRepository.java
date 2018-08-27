package com.futuristlabs.p2p.repos.jdbc;

import com.futuristlabs.p2p.func.useractions.UserActionsLog;
import com.futuristlabs.p2p.func.useractions.UserActionsLogStatus;
import com.futuristlabs.p2p.func.useractions.UserActionsRepository;
import org.joda.time.DateTime;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

@Repository
public class JDBCUserActionsRepository extends JDBCRepository implements UserActionsRepository {

    @Override
    public List<UserActionsLog> modifiedActionsLogs(UUID userId, DateTime modifiedSince) {
        final String sql = onlyModifiedForUser(
                " SELECT id, user_id, life_upgrade_action_id, action_date, times_done " +
                " FROM user_actions_log ");

        final MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("modifiedSince", modifiedSince != null ? modifiedSince.toDate() : null);
        params.addValue("userId", userId.toString());

        return db.returnList(sql, params, new UserActionsLogRowMapper());
    }

    @Override
    public List<UserActionsLog> modifiedActionsLogsRestricted(final UUID userId, final DateTime modifiedSince, final UUID friendId) {
        final String sql = onlyModifiedForUser(
                " SELECT id, user_id, life_upgrade_action_id, action_date, times_done " +
                " FROM user_actions_log ",
                " AND life_upgrade_action_id IN (SELECT life_upgrade_action_id FROM user_permissions WHERE user_id = :userId AND access_to = :friendId AND visible = true) ");

        final MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("modifiedSince", modifiedSince != null ? modifiedSince.toDate() : null);
        params.addValue("userId", userId.toString());
        params.addValue("friendId", friendId.toString());

        return db.returnList(sql, params, new UserActionsLogRowMapper());
    }

    @Override
    public List<UUID> deletedActionsLogs(UUID userId, DateTime modifiedSince) {
        final String sql = deletedSinceForUser("user_actions_log");

        final MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("modifiedSince", modifiedSince != null ? modifiedSince.toDate() : null);
        params.addValue("userId", userId.toString());

        return db.returnList(sql, params, new UUIDMapper());
    }

    @Override
    public void modifyActionsLogs(UUID userId, List<UserActionsLog> userActionsLogs) {
        if (userActionsLogs == null || userActionsLogs.isEmpty()) {
            return;
        }

        final String sql =
                " INSERT INTO user_actions_log (id, user_id, life_upgrade_action_id, action_date, status, times_done) " +
                " VALUES (:id, :userId, :lifeUpgradeActionId, :actionDate, :status, :timesDone) " +
                " ON DUPLICATE KEY UPDATE life_upgrade_action_id = :lifeUpgradeActionId, action_date = :actionDate, status = :status, times_done = :timesDone ";

        for (UserActionsLog userActionsLog : userActionsLogs) {
            final MapSqlParameterSource params = new MapSqlParameterSource();
            params.addValue("id", userActionsLog.getId().toString());
            params.addValue("userId", userId.toString());
            params.addValue("lifeUpgradeActionId", userActionsLog.getLifeUpgradeActionId().toString());
            params.addValue("actionDate", userActionsLog.getActionDateAsDate().toDate());
            params.addValue("status", UserActionsLogStatus.DONE.toString());
            params.addValue("timesDone", userActionsLog.getTimesDone());

            db.update(sql, params);
        }
    }

    @Override
    public void deleteActionsLogs(UUID userId, List<UUID> userActionsLogs) {
        deleteFromTable("user_actions_log", userId, userActionsLogs);
    }

    private static class UserActionsLogRowMapper implements RowMapper<UserActionsLog> {
        @Override
        public UserActionsLog mapRow(ResultSet rs, int rowNum) throws SQLException {
            final UUID id = UUID.fromString(rs.getString("id"));
            final UUID lifeUpgradeActionId = UUID.fromString(rs.getString("life_upgrade_action_id"));
            final DateTime actionDate = new DateTime(rs.getTimestamp("action_date"));
            final int timesDone = rs.getInt("times_done");

            final UserActionsLog log = new UserActionsLog();
            log.setId(id);
            log.setLifeUpgradeActionId(lifeUpgradeActionId);
            log.setActionDateAsDate(actionDate);
            log.setTimesDone(timesDone);
            return log;
        }
    }
}
