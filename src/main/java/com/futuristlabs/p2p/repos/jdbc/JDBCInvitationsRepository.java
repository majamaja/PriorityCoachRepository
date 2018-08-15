package com.futuristlabs.p2p.repos.jdbc;

import com.futuristlabs.p2p.func.invitations.InvitationDetails;
import com.futuristlabs.p2p.func.invitations.InvitationsRepository;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

@Repository
public class JDBCInvitationsRepository extends JDBCRepository implements InvitationsRepository {

    @Override
    public String create(UUID userId, String email, String phone) {
        final String token = UUID.randomUUID().toString();

        final String sql =
                " INSERT INTO invitations (id, user_id, email, phone, token) " +
                " VALUES (:id, :userId, :email, :phone, :token) ";

        final MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("id", UUID.randomUUID().toString());
        params.addValue("userId", userId.toString());
        params.addValue("email", email);
        params.addValue("phone", phone);
        params.addValue("token", token);

        db.update(sql, params);
        return token;
    }

    @Override
    public boolean deleteIfExists(UUID userId, String invitationToken) {
        final String sql = "UPDATE invitations SET invitee_id = :userId WHERE token = :token AND invitee_id IS NULL LIMIT 1";

        final MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("token", invitationToken);
        params.addValue("userId", userId.toString());

        return 1 == db.update(sql, params);
    }

    @Override
    public void markAsFriend(UUID userId, String invitationToken) {
        final String sql =
                " INSERT INTO user_friends " +
                " (id, user_id, friend_id, friend_email, friend_phone) " +
                " SELECT :id, user_id, invitee_id, email, phone FROM invitations WHERE invitee_id = :userId AND token = :token LIMIT 1 ";

        final MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("id", UUID.randomUUID().toString());
        params.addValue("token", invitationToken);
        params.addValue("userId", userId.toString());

        db.update(sql, params);
    }

    @Override
    public InvitationDetails findDetailsByInvitationToken(String invitationToken) {
        final String sql =
                " SELECT id, name, email, phone FROM users " +
                " WHERE id IN (SELECT user_id FROM invitations WHERE token = :invitationToken)";

        final MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("invitationToken", invitationToken);

        return db.returnSafe(sql, params, new RowMapper<InvitationDetails>() {
            @Override
            public InvitationDetails mapRow(ResultSet rs, int rowNum) throws SQLException {
                final UUID id = UUID.fromString(rs.getString("id"));
                final String name = rs.getString("name");
                final String email = rs.getString("email");
                final String phone = rs.getString("phone");

                final InvitationDetails invitationDetails = new InvitationDetails();
                invitationDetails.setId(id);
                invitationDetails.setName(name);
                invitationDetails.setEmail(email);
                invitationDetails.setPhone(phone);
                return invitationDetails;
            }
        });
    }
}
