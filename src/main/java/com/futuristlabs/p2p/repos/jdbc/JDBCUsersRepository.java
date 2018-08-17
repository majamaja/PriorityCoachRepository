package com.futuristlabs.p2p.repos.jdbc;


import com.futuristlabs.p2p.func.auth.AuthenticationRequest;
import com.futuristlabs.p2p.func.auth.Device;
import com.futuristlabs.p2p.func.auth.SessionUser;
import com.futuristlabs.p2p.func.sync.UsersRepository;
import com.futuristlabs.p2p.func.userprofile.UserProfile;
import com.futuristlabs.p2p.utils.Utils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import static com.futuristlabs.p2p.utils.Security.hash;

@Repository
public class JDBCUsersRepository extends JDBCRepository implements UsersRepository {

    @Override
    public boolean existsByEmail(String email) {
        final String sql = "SELECT COUNT(*) FROM users WHERE email = :email";

        final MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("email", email);

        return db.returnBooleanFromLong(sql, params);
    }

    @Override
    public SessionUser findUserByCredentials(AuthenticationRequest authenticationRequest) {
        if (authenticationRequest == null || authenticationRequest.getPassword() == null) {
            return null;
        }

        final String sql = "SELECT id, email FROM native_users WHERE email = :email AND password=:password";

        final MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("email", authenticationRequest.getEmail());
        params.addValue("password", hash(authenticationRequest.getPassword()));

        return db.returnSafe(sql, params, new SessionUserRowMapper());
    }

    @Override
    public SessionUser findUserByFacebook(AuthenticationRequest authenticationRequest) {
        if (authenticationRequest == null || authenticationRequest.getFacebook() == null) {
            return null;
        }

        final String sql = "SELECT u.id, u.email FROM users u JOIN facebook_users fu ON u.facebook_user_id = fu.id WHERE u.email = :email AND fu.fb_id = :fbId";

        final MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("email", authenticationRequest.getEmail());
        params.addValue("fbId", authenticationRequest.getFacebook().getUserId());

        return db.returnSafe(sql, params, new SessionUserRowMapper());
    }

    @Override
    public SessionUser findUserByGooglePlus(AuthenticationRequest authenticationRequest) {
        if (authenticationRequest == null || authenticationRequest.getGplus() == null) {
            return null;
        }

        final String sql = "SELECT u.id, u.email FROM users u JOIN gplus_users gu ON u.gplus_user_id = gu.id WHERE u.email = :email AND gu.gplus_id = :gplusId";

        final MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("email", authenticationRequest.getEmail());
        params.addValue("gplusId", authenticationRequest.getGplus().getUserId());

        return db.returnSafe(sql, params, new SessionUserRowMapper());
    }

    @Transactional
    @Override
    public SessionUser createWithNative(AuthenticationRequest authenticationRequest) {
        final UUID id = UUID.randomUUID();
        final String email = authenticationRequest.getEmail();
        final String name = authenticationRequest.getName();
        final String password = authenticationRequest.getPassword();

        createNativeUser(id, email, password);
        createUser(id, name, email, id, null, null);
        return new SessionUser(id, email);
    }

    @Transactional
    @Override
    public SessionUser createWithFacebook(AuthenticationRequest authenticationRequest) {
        if (authenticationRequest == null || authenticationRequest.getFacebook() == null) {
            return null;
        }

        final UUID id = UUID.randomUUID();
        final String email = authenticationRequest.getEmail();
        final String name = authenticationRequest.getName();
        final String sql = "INSERT INTO facebook_users (id, fb_id, fb_token) VALUES (:id, :fbId, :fbToken)";

        final MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("id", id.toString());
        params.addValue("fbId", authenticationRequest.getFacebook().getUserId());
        params.addValue("fbToken", authenticationRequest.getFacebook().getTokenId());

        db.insert(sql, params);
        createUser(id, name, email, null, id, null);
        return new SessionUser(id, email);
    }

    @Transactional
    @Override
    public SessionUser createWithGooglePlus(AuthenticationRequest authenticationRequest) {
        if (authenticationRequest == null || authenticationRequest.getGplus() == null) {
            return null;
        }

        final UUID id = UUID.randomUUID();
        final String email = authenticationRequest.getEmail();
        final String name = authenticationRequest.getName();
        final String sql = "INSERT INTO gplus_users (id, gplus_id, gplus_token) VALUES (:id, :gplusId, :gplusToken)";

        final MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("id", id.toString());
        params.addValue("gplusId", authenticationRequest.getGplus().getUserId());
        params.addValue("gplusToken", authenticationRequest.getGplus().getTokenId());

        db.insert(sql, params);
        createUser(id, name, email, null, null, id);
        return new SessionUser(id, email);
    }

    @Transactional
    @Override
    public void registerUserDevice(UUID userId, Device device) {
        if (device == null) {
            return;
        }

        final String sql =
                "INSERT INTO user_devices (user_id, device_id, type, apn_token) " +
                "VALUES (:userId, :deviceId, :type, :apnToken) " +
                "ON DUPLICATE KEY UPDATE type = :type, apn_token = :apnToken ";

        final MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("userId", userId.toString());
        params.addValue("deviceId", device.getUdid());
        params.addValue("type", device.getType());
        params.addValue("apnToken", device.getApnToken());

        db.insert(sql, params);
    }

    @Override
    public void updatePassword(String email, String newPassword) {
        if (email == null || newPassword == null) {
            return;
        }

        final String sql = "UPDATE native_users SET password = :newPassword WHERE email = :email";

        final MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("email", email);
        params.addValue("newPassword", hash(newPassword));

        db.update(sql, params);
    }

    @Override
    public UserProfile findProfileById(final UUID userId) {
        final String sql = "SELECT id, name, email FROM users WHERE id = :userId";

        final MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("userId", userId.toString());

        return db.returnSafe(sql, params, new BeanPropertyRowMapper<>(UserProfile.class));
    }

    @Override
    public void updateProfile(final UserProfile userProfile) {
        updateBaseUserProfile(userProfile);
        updateNativeUserProfile(userProfile);
    }

    private void updateBaseUserProfile(final UserProfile userProfile) {
        final String sql = "UPDATE users SET name = :name, email = :email WHERE id = :userId";

        final MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("userId", userProfile.getId().toString());
        params.addValue("email", userProfile.getEmail());
        params.addValue("name", userProfile.getName());

        db.update(sql, params);
    }

    private void updateNativeUserProfile(final UserProfile userProfile) {
        final String sql = "UPDATE native_users SET email = :email WHERE id = :userId";

        final MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("userId", userProfile.getId().toString());
        params.addValue("email", userProfile.getEmail());

        db.update(sql, params);
    }

    private void createUser(UUID id, String name, String email, UUID nativeUserId, UUID facebookUserId, UUID gplusUserId) {
        final String sql = "INSERT INTO users (id, name, email, native_user_id, facebook_user_id, gplus_user_id) VALUES (:id, :name, :email, :nativeUserId, :facebookUserId, :gplusUserId)";

        final MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("id", id.toString());
        params.addValue("name", name);
        params.addValue("email", email);

        params.addValue("nativeUserId", Utils.toString(nativeUserId));
        params.addValue("facebookUserId", Utils.toString(facebookUserId));
        params.addValue("gplusUserId", Utils.toString(gplusUserId));

        db.insert(sql, params);
    }

    private void createNativeUser(UUID id, String email, String password) {
        final String sql = "INSERT INTO native_users (id, email, password) VALUES (:id, :email, :password)";

        final MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("id", id.toString());
        params.addValue("email", email);
        params.addValue("password", hash(password));

        db.insert(sql, params);
    }

    private static class SessionUserRowMapper implements RowMapper<SessionUser> {
        @Override
        public SessionUser mapRow(ResultSet rs, int rowNum) throws SQLException {
            final UUID id = UUID.fromString(rs.getString("id"));
            final String email = rs.getString("email");

            return new SessionUser(id, email);
        }
    }
}
