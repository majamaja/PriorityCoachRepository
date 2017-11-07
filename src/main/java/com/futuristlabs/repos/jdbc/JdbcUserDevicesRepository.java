package com.futuristlabs.repos.jdbc;

import com.futuristlabs.func.users.devices.UserDevice;
import com.futuristlabs.func.users.devices.UserDevicesRepository;
import com.futuristlabs.repos.jdbc.common.AbstractJdbcRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Repository
public class JdbcUserDevicesRepository extends AbstractJdbcRepository implements UserDevicesRepository {
    @Override
    @Transactional
    public void register(UUID userId, UserDevice device) {
        final String sql =
                " INSERT INTO user_devices (installation_id, user_id, type, token) " +
                "     VALUES (:installationId, :userId, :type, :token) ON CONFLICT DO NOTHING ";
        db.insert(sql, device);
    }

    @Override
    public List<UserDevice> getByUserId(UUID userId) {
        final String sql = "SELECT * FROM user_devices WHERE user_id = :userId";
        return db.list(UserDevice.class, sql, set("userId", userId));
    }

    @Override
    @Transactional
    public void deleteById(UUID installationId) {
        final String sql = "DELETE FROM user_devices WHERE installation_id = :installationId";
        db.update(sql, set("installationId", installationId));
    }
}
