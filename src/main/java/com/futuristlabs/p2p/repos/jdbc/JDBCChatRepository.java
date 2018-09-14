package com.futuristlabs.p2p.repos.jdbc;

import com.futuristlabs.p2p.func.chat.ChatMessage;
import com.futuristlabs.p2p.func.chat.ChatRepository;
import com.futuristlabs.p2p.func.chat.IncomingChatMessage;
import org.joda.time.DateTime;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

@Repository
public class JDBCChatRepository extends JDBCRepository implements ChatRepository {

    @Override
    public List<ChatMessage> findAllByFriendshipAndUpdatedAfter(UUID userId, UUID friendshipId, DateTime modifiedSince) {
        final String sql = onlyModified(
                " SELECT id, user_id_from, user_id_to, message, send_at FROM user_messages ",
                " AND ((user_id_from, user_id_to) IN (select user_id, friend_id from user_friends where id = :friendshipId) " +
                " OR   (user_id_from, user_id_to) IN (select friend_id, user_id from user_friends where id = :friendshipId)) " +
                " AND (user_id_from = :userId OR user_id_to = :userId) ");

        final MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("modifiedSince", modifiedSince != null ? modifiedSince.toDate() : null);
        params.addValue("userId", userId.toString());
        params.addValue("friendshipId", friendshipId.toString());

        return db.returnList(sql, params, (rs, rowNum) -> {
            final ChatMessage message = new ChatMessage();
            message.setId(UUID.fromString(rs.getString("id")));
            message.setFrom(UUID.fromString(rs.getString("user_id_from")));
            message.setTo(UUID.fromString(rs.getString("user_id_to")));
            message.setContent(rs.getString("message"));
            message.setSendAtAsDate(new DateTime(rs.getTimestamp("send_at")));
            return message;
        });
    }

    @Override
    public List<String> findAllDeviceTokensForUserFriend(UUID fromUserId, UUID friendshipId) {
        final String sql =
                " SELECT apn_token " +
                " FROM user_devices " +
                " WHERE apn_token IS NOT NULL " +
                " AND user_id IN (" +
                        " SELECT user_id FROM user_friends WHERE id = :friendshipId AND friend_id = :fromUserId " +
                        " UNION ALL " +
                        " SELECT friend_id FROM user_friends WHERE id = :friendshipId AND user_id = :fromUserId " +
                " ) ";

        final MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("friendshipId", friendshipId);
        params.addValue("fromUserId", fromUserId);

        return db.returnList(sql, params, new RowMapper<String>() {
                    @Override
                    public String mapRow(ResultSet rs, int rowNum) throws SQLException {
                        return rs.getString(1);
                    }
                });
    }

    @Override
    public void save(IncomingChatMessage message, UUID fromUserId, UUID forFriendshipId) {
        final String sql =
                " INSERT INTO user_messages (id, user_id_from, user_id_to, message, send_at) " +
                " SELECT :id, user_id as user_id_from, friend_id as user_id_to, :message, :sendAt FROM user_friends WHERE id = :friendshipId AND user_id = :fromUser " +
                " UNION ALL " +
                " SELECT :id, friend_id as user_id_from, user_id as user_id_to, :message, :sendAt FROM user_friends WHERE id = :friendshipId AND friend_id = :fromUser ";

        final MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("id", message.getId().toString());
        params.addValue("fromUser", fromUserId.toString());
        params.addValue("friendshipId", forFriendshipId.toString());
        params.addValue("message", message.getContent());
        params.addValue("sendAt", message.getSendAtDate().toDate());

        db.update(sql, params);
    }

}
