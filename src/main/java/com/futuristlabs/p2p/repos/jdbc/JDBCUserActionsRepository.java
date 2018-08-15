package com.futuristlabs.p2p.repos.jdbc;

import com.futuristlabs.p2p.func.useractions.*;
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
    public List<UserActionItem> modifiedActionItems(UUID userId, DateTime modifiedSince) {
        final String sql = onlyModifiedForUser(
                " SELECT id, user_id, action_id, start_date, frequency, times_per_day, every_x_day, day_of_week_mon, day_of_week_tue, day_of_week_wed, day_of_week_thr, day_of_week_fri, day_of_week_sat, day_of_week_sun " +
                " FROM user_action_items ");

        final MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("modifiedSince", modifiedSince != null ? modifiedSince.toDate() : null);
        params.addValue("userId", userId.toString());

        return db.returnList(sql, params, new UserActionItemRowMapper());
    }

    @Override
    public List<UUID> deletedActionItems(UUID userId, DateTime modifiedSince) {
        final String sql = deletedSinceForUser("user_action_items");

        final MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("modifiedSince", modifiedSince != null ? modifiedSince.toDate() : null);
        params.addValue("userId", userId.toString());

        return db.returnList(sql, params, new UUIDMapper());
    }

    @Override
    public List<UserActionsLog> modifiedActionsLogs(UUID userId, DateTime modifiedSince) {
        final String sql = onlyModifiedForUser(
                " SELECT id, user_id, user_action_item_id, action_date, times_done " +
                " FROM user_actions_log ");

        final MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("modifiedSince", modifiedSince != null ? modifiedSince.toDate() : null);
        params.addValue("userId", userId.toString());

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
    public void modifyActionItems(UUID userId, List<UserActionItem> userActionItems) {
        if (userActionItems == null || userActionItems.isEmpty()) {
            return;
        }

        final String sql =
                " INSERT INTO user_action_items (id, user_id, action_id, start_date, frequency, times_per_day, every_x_day, day_of_week_mon, day_of_week_tue, day_of_week_wed, day_of_week_thr, day_of_week_fri, day_of_week_sat, day_of_week_sun) " +
                " VALUES (:id, :userId, :actionId, :startDate, :frequency, :timesPerDay, :everyXDay, :dayOfWeekMon, :dayOfWeekTue, :dayOfWeekWed, :dayOfWeekThr, :dayOfWeekFri, :dayOfWeekSat, :dayOfWeekSun) " +
                " ON DUPLICATE KEY UPDATE action_id = :actionId, start_date = :startDate, frequency = :frequency, times_per_day = :timesPerDay, every_x_day = :everyXDay, day_of_week_mon = :dayOfWeekMon, day_of_week_tue = :dayOfWeekTue, day_of_week_wed = :dayOfWeekWed, day_of_week_thr = :dayOfWeekThr, day_of_week_fri = :dayOfWeekFri, day_of_week_sat = :dayOfWeekSat, day_of_week_sun = :dayOfWeekSun ";

        for (UserActionItem userActionItem : userActionItems) {
            final MapSqlParameterSource params = new MapSqlParameterSource();
            params.addValue("id", userActionItem.getId().toString());
            params.addValue("userId", userId.toString());
            params.addValue("actionId", userActionItem.getActionId().toString());
            params.addValue("startDate", userActionItem.getStartDateAsDate().toDate());
            params.addValue("frequency", userActionItem.getFrequency().toString());
            params.addValue("timesPerDay", userActionItem.getTimesPerDay());
            params.addValue("everyXDay", userActionItem.getEveryXDay());
            params.addValue("dayOfWeekMon", userActionItem.isDayOfWeekMon());
            params.addValue("dayOfWeekTue", userActionItem.isDayOfWeekTue());
            params.addValue("dayOfWeekWed", userActionItem.isDayOfWeekWed());
            params.addValue("dayOfWeekThr", userActionItem.isDayOfWeekThr());
            params.addValue("dayOfWeekFri", userActionItem.isDayOfWeekFri());
            params.addValue("dayOfWeekSat", userActionItem.isDayOfWeekSat());
            params.addValue("dayOfWeekSun", userActionItem.isDayOfWeekSun());

            db.update(sql, params);
        }
    }

    @Override
    public void modifyActionsLogs(UUID userId, List<UserActionsLog> userActionsLogs) {
        if (userActionsLogs == null || userActionsLogs.isEmpty()) {
            return;
        }

        final String sql =
                " INSERT INTO user_actions_log (id, user_id, user_action_item_id, action_date, status, times_done) " +
                " VALUES (:id, :userId, :userActionItemId, :actionDate, :status, :timesDone) " +
                " ON DUPLICATE KEY UPDATE user_action_item_id = :userActionItemId, action_date = :actionDate, status = :status, times_done = :timesDone ";

        for (UserActionsLog userActionsLog : userActionsLogs) {
            final MapSqlParameterSource params = new MapSqlParameterSource();
            params.addValue("id", userActionsLog.getId().toString());
            params.addValue("userId", userId.toString());
            params.addValue("userActionItemId", userActionsLog.getUserActionItemId().toString());
            params.addValue("actionDate", userActionsLog.getActionDateAsDate().toDate());
            params.addValue("status", UserActionsLogStatus.DONE.toString());
            params.addValue("timesDone", userActionsLog.getTimesDone());

            db.update(sql, params);
        }
    }

    @Override
    public void deleteActionItems(UUID userId, List<UUID> userActionItems) {
        deleteFromTable("user_action_items", userId, userActionItems);
    }

    @Override
    public void deleteActionsLogs(UUID userId, List<UUID> userActionsLogs) {
        deleteFromTable("user_actions_log", userId, userActionsLogs);
    }

    private static class UserActionItemRowMapper implements RowMapper<UserActionItem> {
        @Override
        public UserActionItem mapRow(ResultSet rs, int rowNum) throws SQLException {
            final UUID id = UUID.fromString(rs.getString("id"));
            final UUID userId = UUID.fromString(rs.getString("user_id"));
            final UUID actionId = UUID.fromString(rs.getString("action_id"));
            final DateTime startDate = new DateTime(rs.getTimestamp("start_date"));
            final UserActionItemFrequency frequency = UserActionItemFrequency.valueOf(rs.getString("frequency"));
            final int timesPerDay = rs.getInt("times_per_day");
            final int everyXDay = rs.getInt("every_x_day");
            final boolean dayOfWeekMon = rs.getBoolean("day_of_week_mon");
            final boolean dayOfWeekTue = rs.getBoolean("day_of_week_tue");
            final boolean dayOfWeekWed = rs.getBoolean("day_of_week_wed");
            final boolean dayOfWeekThr = rs.getBoolean("day_of_week_thr");
            final boolean dayOfWeekFri = rs.getBoolean("day_of_week_fri");
            final boolean dayOfWeekSat = rs.getBoolean("day_of_week_sat");
            final boolean dayOfWeekSun = rs.getBoolean("day_of_week_sun");

            final UserActionItem item = new UserActionItem();
            item.setId(id);
            item.setUserId(userId);
            item.setActionId(actionId);
            item.setStartDateAsDate(startDate);
            item.setFrequency(frequency);
            item.setTimesPerDay(timesPerDay);
            item.setEveryXDay(everyXDay);
            item.setDayOfWeekMon(dayOfWeekMon);
            item.setDayOfWeekTue(dayOfWeekTue);
            item.setDayOfWeekWed(dayOfWeekWed);
            item.setDayOfWeekThr(dayOfWeekThr);
            item.setDayOfWeekFri(dayOfWeekFri);
            item.setDayOfWeekSat(dayOfWeekSat);
            item.setDayOfWeekSun(dayOfWeekSun);
            return item;
        }
    }

    private static class UserActionsLogRowMapper implements RowMapper<UserActionsLog> {
        @Override
        public UserActionsLog mapRow(ResultSet rs, int rowNum) throws SQLException {
            final UUID id = UUID.fromString(rs.getString("id").toString());
            final UUID userActionItemId = UUID.fromString(rs.getString("user_action_item_id"));
            final DateTime actionDate = new DateTime(rs.getTimestamp("action_date"));
            final int timesDone = rs.getInt("times_done");

            final UserActionsLog log = new UserActionsLog();
            log.setId(id);
            log.setUserActionItemId(userActionItemId);
            log.setActionDateAsDate(actionDate);
            log.setTimesDone(timesDone);
            return log;
        }
    }
}
