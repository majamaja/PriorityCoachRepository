package com.futuristlabs.p2p.repos.jdbc;

import com.futuristlabs.p2p.func.lifeupgrade.LifeUpgradeAction;
import com.futuristlabs.p2p.func.lifeupgrade.LifeUpgradeCategory;
import com.futuristlabs.p2p.func.lifeupgrade.ReferenceRepository;
import org.joda.time.DateTime;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

@Repository
public class JDBCReferenceRepository extends JDBCRepository implements ReferenceRepository {

    @Override
    public List<LifeUpgradeCategory> modifiedLifeUpgradeCategories(DateTime modifiedSince) {
        final String sql =
                " SELECT id, name, icon FROM life_upgrade_categories " +
                " WHERE is_deleted = false " +
                " AND (:modifiedSince IS NULL OR last_modified > :modifiedSince)";

        final MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("modifiedSince", modifiedSince != null ? modifiedSince.toDate() : null);

        return db.returnList(sql, params, new RowMapper<LifeUpgradeCategory>() {
            @Override
            public LifeUpgradeCategory mapRow(ResultSet rs, int rowNum) throws SQLException {
                final UUID id = UUID.fromString(rs.getString("id"));
                final String name = rs.getString("name");
                final byte[] icon = rs.getBytes("icon");

                return new LifeUpgradeCategory(id, name, icon);
            }
        });
    }

    @Override
    public List<UUID> deletedLifeUpgradeCategories(DateTime modifiedSince) {
        final String sql =
                " SELECT id FROM life_upgrade_categories " +
                " WHERE is_deleted = true " +
                " AND (:modifiedSince IS NULL OR last_modified > :modifiedSince)";

        final MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("modifiedSince", modifiedSince != null ? modifiedSince.toDate() : null);

        return db.returnList(sql, params, new UUIDMapper());
    }

    @Override
    public void modifyLifeUpgradeCategory(LifeUpgradeCategory category) {
        if (category == null) {
            return;
        }

        final String sql =
                " INSERT INTO life_upgrade_categories (id, name, icon) " +
                " VALUES (:id, :name, :icon) " +
                " ON DUPLICATE KEY UPDATE name = :name, icon = :icon";

        final MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("id", category.getId().toString());
        params.addValue("name", category.getName());
        params.addValue("icon", category.getIcon());

        db.update(sql, params);
    }

    @Override
    public void deleteLifeUpgradeCategory(UUID categoryId) {
        if (categoryId == null) {
            return;
        }

        final String sql =
                " UPDATE life_upgrade_categories " +
                " SET is_deleted = true " +
                " WHERE is_deleted = false " +
                " AND id = :categoryId ";

        final MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("categoryId", categoryId.toString());

        db.update(sql, params);
    }

    @Override
    public List<LifeUpgradeAction> modifiedLifeUpgradeActionsForCategory(UUID categoryId) {
        if (categoryId == null) {
            return new ArrayList<>();
        }

        final String sql =
                " SELECT id, name, life_upgrade_category_id " +
                " FROM life_upgrade_actions " +
                " WHERE is_custom = false " +
                " AND user_id IS NULL " +
                " AND is_deleted = false " +
                " AND life_upgrade_category_id = :categoryId ";

        final MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("categoryId", categoryId.toString());

        return db.returnList(sql, params, new LifeUpgradeActionRowMapper());
    }

    @Override
    public List<LifeUpgradeAction> modifiedLifeUpgradeActions(DateTime modifiedSince) {
        final String sql =
                " SELECT id, name, life_upgrade_category_id " +
                " FROM life_upgrade_actions " +
                " WHERE is_custom = false " +
                " AND user_id IS NULL " +
                " AND is_deleted = false " +
                " AND (:modifiedSince IS NULL OR last_modified > :modifiedSince)";

        final MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("modifiedSince", modifiedSince != null ? modifiedSince.toDate() : null);

        return db.returnList(sql, params, new LifeUpgradeActionRowMapper());
    }

    @Override
    public List<UUID> deletedLifeUpgradeActions(DateTime modifiedSince) {
        final String sql =
                " SELECT id FROM life_upgrade_actions " +
                " WHERE is_custom = false " +
                " AND user_id IS NULL " +
                " AND is_deleted = true " +
                " AND (:modifiedSince IS NULL OR last_modified > :modifiedSince)";

        final MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("modifiedSince", modifiedSince != null ? modifiedSince.toDate() : null);

        return db.returnList(sql, params, new UUIDMapper());
    }

    @Override
    public void modifyLifeUpgradeAction(LifeUpgradeAction lifeUpgradeAction) {
        if (lifeUpgradeAction == null) {
            return;
        }

        final String sql =
                " INSERT INTO life_upgrade_actions (id, life_upgrade_category_id, name, is_custom) " +
                " VALUES (:id, :lifeUpgradeCategoryId, :name, false) " +
                " ON DUPLICATE KEY UPDATE name = :name ";

        final MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("id", lifeUpgradeAction.getId().toString());
        params.addValue("lifeUpgradeCategoryId", lifeUpgradeAction.getLifeUpgradeCategoryId().toString());
        params.addValue("name", lifeUpgradeAction.getName());

        db.update(sql, params);
    }

    @Override
    public void deleteLifeUpgradeAction(UUID actionId) {
        if (actionId == null) {
            return;
        }

        final String sql =
                " UPDATE life_upgrade_actions " +
                " SET is_deleted = true " +
                " WHERE is_custom = false " +
                " AND is_deleted = false " +
                " AND id = :actionId ";

        final MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("actionId", actionId.toString());

        db.update(sql, params);
    }

    @Override
    public List<LifeUpgradeAction> modifiedUserLifeUpgradeActions(UUID userId, DateTime modifiedSince) {
        final String sql =
                " SELECT id, name, life_upgrade_category_id " +
                " FROM life_upgrade_actions " +
                " WHERE is_custom = true " +
                " AND user_id = :userId " +
                " AND is_deleted = false " +
                " AND (:modifiedSince IS NULL OR last_modified > :modifiedSince)";

        final MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("modifiedSince", modifiedSince != null ? modifiedSince.toDate() : null);
        params.addValue("userId", userId.toString());

        return db.returnList(sql, params, new LifeUpgradeActionRowMapper());
    }

    @Override
    public List<UUID> deletedUserLifeUpgradeActions(UUID userId, DateTime modifiedSince) {
        final String sql =
                " SELECT id FROM life_upgrade_actions " +
                " WHERE is_custom = true " +
                " AND user_id = :userId " +
                " AND is_deleted = true " +
                " AND (:modifiedSince IS NULL OR last_modified > :modifiedSince)";

        final MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("modifiedSince", modifiedSince != null ? modifiedSince.toDate() : null);
        params.addValue("userId", userId.toString());

        return db.returnList(sql, params, new UUIDMapper());
    }

    @Override
    public void modifyUserLifeUpgradeActions(UUID userId, List<LifeUpgradeAction> lifeUpgradeActions) {
        if (lifeUpgradeActions == null || lifeUpgradeActions.isEmpty()) {
            return;
        }

        final String sql =
                " INSERT INTO life_upgrade_actions (id, user_id, life_upgrade_category_id, name, is_custom) " +
                " VALUES (:id, :userId, :lifeUpgradeCategoryId, :name, true) " +
                " ON DUPLICATE KEY UPDATE name = :name ";

        for (LifeUpgradeAction lifeUpgradeAction : lifeUpgradeActions) {
            final MapSqlParameterSource params = new MapSqlParameterSource();
            params.addValue("id", lifeUpgradeAction.getId().toString());
            params.addValue("userId", userId.toString());
            params.addValue("lifeUpgradeCategoryId", lifeUpgradeAction.getLifeUpgradeCategoryId().toString());
            params.addValue("name", lifeUpgradeAction.getName());

            db.update(sql, params);
        }
    }

    @Override
    public void deleteUserLifeUpgradeActions(UUID userId, List<UUID> lifeUpgradeActions) {
        if (lifeUpgradeActions == null || lifeUpgradeActions.isEmpty()) {
            return;
        }

        final String sql =
                " UPDATE life_upgrade_actions " +
                " SET is_deleted = true " +
                " WHERE is_custom = true " +
                " AND user_id = :userId " +
                " AND is_deleted = false " +
                " AND id IN (:lifeUpgradeActions) ";

        final MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("userId", userId.toString());
        params.addValue("lifeUpgradeActions", toStringList(lifeUpgradeActions));

        db.update(sql, params);
    }

    private static class LifeUpgradeActionRowMapper implements RowMapper<LifeUpgradeAction> {
        @Override
        public LifeUpgradeAction mapRow(ResultSet rs, int rowNum) throws SQLException {
            final UUID id = UUID.fromString(rs.getString("id"));
            final String name = rs.getString("name");
            final UUID lifeUpgradeCategoryId = UUID.fromString(rs.getString("life_upgrade_category_id"));

            final LifeUpgradeAction lifeUpgradeAction = new LifeUpgradeAction();
            lifeUpgradeAction.setId(id);
            lifeUpgradeAction.setName(name);
            lifeUpgradeAction.setLifeUpgradeCategoryId(lifeUpgradeCategoryId);
            lifeUpgradeAction.setCustom(false);
            return lifeUpgradeAction;
        }
    }
}
