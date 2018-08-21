package com.futuristlabs.p2p.repos.jdbc;

import com.futuristlabs.p2p.func.happiness.HappinessLevel;
import com.futuristlabs.p2p.func.happiness.UserHappinessRepository;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public class JDBCUserHappinessRepository extends JDBCRepository implements UserHappinessRepository {

    @Override
    public List<HappinessLevel> modifiedHappinessLevel(UUID userId, DateTime modifiedSince) {
        final String sql =
                " SELECT id, level, checkin_date, user_id " +
                " FROM happiness_level_checkins " +
                " WHERE (:modifiedSince IS NULL OR last_modified > :modifiedSince) " +
                " AND user_id = :userId ";

        final MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("modifiedSince", modifiedSince != null ? modifiedSince.toDate() : null);
        params.addValue("userId", userId.toString());

        return db.returnList(sql, params, (rs, rowNum) -> {
            final UUID id = UUID.fromString(rs.getString("id"));
            final int happinessLevel = rs.getInt("level");
            final LocalDate checkinDate = new LocalDate(rs.getDate("checkin_date"));
            final UUID userId1 = UUID.fromString(rs.getString("user_id"));

            return new HappinessLevel(id, happinessLevel, checkinDate, userId1);
        });

    }

    @Override
    public void modifyHappinessLevel(UUID userId, List<HappinessLevel> userHappinessLevels) {
        if (userHappinessLevels == null || userHappinessLevels.isEmpty()) {
            return;
        }

        final String sql =
                " INSERT INTO happiness_level_checkins (id, user_id, level, checkin_date) " +
                " VALUES (:id, :userId, :level, :checkinDate) " +
                " ON DUPLICATE KEY UPDATE level = :level, checkin_date = :checkinDate ";

        for (HappinessLevel userHappinessLevel : userHappinessLevels) {
            final MapSqlParameterSource params = new MapSqlParameterSource();
            params.addValue("id", userHappinessLevel.getId().toString());
            params.addValue("userId", userId.toString());
            params.addValue("level", userHappinessLevel.getLevel());
            params.addValue("checkinDate", userHappinessLevel.getCheckinDateAsDate().toDate());


            db.update(sql, params);
        }
    }
}
