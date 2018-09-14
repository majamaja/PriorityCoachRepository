package com.futuristlabs.p2p.repos.jdbc;

import com.futuristlabs.p2p.func.buddy.UserFriend;
import com.futuristlabs.p2p.func.buddy.UserFriendPermission;
import com.futuristlabs.p2p.func.buddy.UserFriendsRepository;
import org.joda.time.DateTime;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

@Repository
public class JDBCUserFriendsRepository extends JDBCRepository implements UserFriendsRepository {

    @Override
    public List<UserFriend> findAllFriends(final UUID userId) {
        final String sql =
                " SELECT uf.id, uf.user_id, uf.friend_id, u.name as friend_name, uf.friend_email, uf.friend_phone " +
                " FROM user_friends uf " +
                " LEFT JOIN users u ON uf.friend_id = u.id " +
                " WHERE uf.is_deleted = false " +
                " AND (uf.user_id = :userId OR uf.friend_id = :userId) ";

        final MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("userId", userId.toString());

        return db.returnList(sql, params, new UserFriendRowMapper(userId));
    }

    @Override
    public List<UserFriendPermission> modifiedFriendsPermissions(UUID userId, DateTime modifiedSince) {
        final String sql =
                " SELECT ufp.id, ufp.user_id, ufp.friendship_id, ufp.life_upgrade_action_id, ufp.visible " +
                " FROM user_friend_permissions ufp " +
                " JOIN user_friends uf ON ufp.friendship_id = uf.id " +
                " WHERE (:modifiedSince IS NULL OR ufp.last_modified > :modifiedSince) " +
                " AND (uf.user_id = :userId OR uf.friend_id = :userId) " +
                " AND uf.is_deleted = false ";

        final MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("modifiedSince", modifiedSince != null ? modifiedSince.toDate() : null);
        params.addValue("userId", userId.toString());

        return db.returnList(sql, params, new UserFriendPermissionRowMapper());
    }

    @Override
    public List<UUID> deletedFriends(UUID userId, DateTime modifiedSince) {
        final String sql = deletedSinceForUser("user_friends");

        final MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("modifiedSince", modifiedSince != null ? modifiedSince.toDate() : null);
        params.addValue("userId", userId.toString());

        return db.returnList(sql, params, new UUIDMapper());
    }

    @Override
    public List<UUID> deletedFriendsPermissions(UUID userId, DateTime modifiedSince) {
        final String sql =
                " SELECT id FROM user_friend_permissions " +
                " WHERE visible = false " +
                " AND (:modifiedSince IS NULL OR last_modified > :modifiedSince) " +
                " AND user_id = :userId ";

        final MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("modifiedSince", modifiedSince != null ? modifiedSince.toDate() : null);
        params.addValue("userId", userId.toString());

        return db.returnList(sql, params, new UUIDMapper());
    }

    @Override
    public void modifyFriends(UUID userId, List<UserFriend> userFriends) {
        if (userFriends == null || userFriends.isEmpty()) {
            return;
        }

        final String sql =
                " INSERT INTO user_friends (id, user_id, friend_id, friend_email, friend_phone) " +
                " VALUES (:id, :userId, :friendId, :friendEmail, :friendPhone) " +
                " ON DUPLICATE KEY UPDATE friend_id = :friendId, friend_email = :friendEmail, friend_phone = :friendPhone ";

        for (UserFriend userFriend : userFriends) {
            final MapSqlParameterSource params = new MapSqlParameterSource();
            params.addValue("id", userFriend.getId().toString());
            params.addValue("userId", userId.toString());
            params.addValue("friendId", userFriend.getFriendId() == null ? null : userFriend.getFriendId().toString());
            params.addValue("friendEmail", userFriend.getFriendEmail());
            params.addValue("friendPhone", userFriend.getFriendPhone());

            db.update(sql, params);
        }
    }

    @Override
    public void modifyFriendsPermissions(UUID userId, List<UserFriendPermission> userFriendPermissions) {
        if (userFriendPermissions == null || userFriendPermissions.isEmpty()) {
            return;
        }

        final String sql =
                " INSERT INTO user_friend_permissions (id, user_id, friendship_id, life_upgrade_action_id, visible) " +
                " VALUES (:id, :userId, :friendshipId, :lifeUpgradeActionId, :visible) " +
                " ON DUPLICATE KEY UPDATE friendship_id = :friendshipId, life_upgrade_action_id = :lifeUpgradeActionId, visible = :visible ";

        for (UserFriendPermission userFriendPermission : userFriendPermissions) {
            final MapSqlParameterSource params = new MapSqlParameterSource();
            params.addValue("id", userFriendPermission.getId().toString());
            params.addValue("userId", userId.toString());
            params.addValue("friendshipId", userFriendPermission.getFriendshipId().toString());
            params.addValue("lifeUpgradeActionId", userFriendPermission.getLifeUpgradeActionsId().toString());
            params.addValue("visible", userFriendPermission.isVisible());

            db.update(sql, params);
        }
    }

    @Override
    public void deleteFriends(UUID userId, List<UUID> userFriendships) {
        if (userFriendships == null || userFriendships.isEmpty()) {
            return;
        }

        final String sql = "UPDATE user_friends SET is_deleted = true WHERE (user_id = :userId OR friend_id = :userId) AND id IN (:ids)";

        final MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("userId", userId.toString());
        params.addValue("ids", toStringList(userFriendships));

        db.update(sql, params);

        for (UUID userFriendshipId : userFriendships) {
            deleteFriendsPermissionsByFriedship(userId, userFriendshipId);
        }
    }

    @Override
    public void deleteFriendsPermissions(UUID userId, List<UUID> userFriendPermissions) {
        if (userFriendPermissions == null || userFriendPermissions.isEmpty()) {
            return;
        }

        final String sql = "UPDATE user_friend_permissions SET visible = false WHERE user_id = :userId AND id IN (:ids)";

        final MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("userId", userId.toString());
        params.addValue("ids", toStringList(userFriendPermissions));

        db.update(sql, params);
    }

    private void deleteFriendsPermissionsByFriedship(UUID userId, UUID friendshipId) {
        final String sql = "UPDATE user_friend_permissions SET visible = false WHERE user_id = :userId AND friendship_id = :friendshipId ";

        final MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("userId", userId.toString());
        params.addValue("friendshipId", friendshipId.toString());

        db.update(sql, params);
    }

    private static class UserFriendRowMapper implements RowMapper<UserFriend> {
        private final UUID userId;

        public UserFriendRowMapper(UUID userId) {
            this.userId = userId;
        }

        @Override
        public UserFriend mapRow(ResultSet rs, int rowNum) throws SQLException {
            final UUID id = UUID.fromString(rs.getString("id"));
            final UUID userAId = UUID.fromString(rs.getString("user_id"));
            final UUID userBId = UUID.fromString(rs.getString("friend_id"));
            final UUID friendId = userAId.equals(userId) ? userBId : userAId;
            final String friendName = rs.getString("friend_name");
            final String friendEmail = rs.getString("friend_email");
            final String friendPhone = rs.getString("friend_phone");

            final UserFriend friend = new UserFriend();
            friend.setId(id);
            friend.setFriendId(friendId);
            friend.setFriendName(friendName);
            friend.setFriendEmail(friendEmail);
            friend.setFriendPhone(friendPhone);
            return friend;
        }
    }

    private static class UserFriendPermissionRowMapper implements RowMapper<UserFriendPermission> {
        @Override
        public UserFriendPermission mapRow(ResultSet rs, int rowNum) throws SQLException {
            final UUID id = UUID.fromString(rs.getString("id"));
            final UUID friendshipId = UUID.fromString(rs.getString("friendship_id"));
            final UUID lifeUpgradeActionId = UUID.fromString(rs.getString("life_upgrade_action_id"));
            final boolean visible = rs.getBoolean("visible");

            final UserFriendPermission friendPermission = new UserFriendPermission();
            friendPermission.setId(id);
            friendPermission.setFriendshipId(friendshipId);
            friendPermission.setLifeUpgradeActionsId(lifeUpgradeActionId);
            friendPermission.setVisible(visible);
            return friendPermission;
        }
    }
}
