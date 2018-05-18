package com.futuristlabs.repos.jdbc;

import com.futuristlabs.func.admins.AdminUser;
import com.futuristlabs.func.admins.AdminUserSearch;
import com.futuristlabs.func.admins.AdminUsersRepository;
import com.futuristlabs.repos.jdbc.common.AbstractJdbcRepository;
import com.futuristlabs.repos.jdbc.common.Parameters;
import com.futuristlabs.utils.crypto.CryptoUtils;
import com.futuristlabs.utils.repository.Page;
import com.futuristlabs.utils.repository.PageData;
import com.futuristlabs.utils.repository.SortBy;
import com.futuristlabs.utils.repository.SortOrder;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public class JdbcAdminUsersRepository extends AbstractJdbcRepository implements AdminUsersRepository {
    private String encrypt(final String password) {
        return CryptoUtils.encodePassword(password);
    }

    @Override
    public PageData<AdminUser> findPaged(Page page, SortBy<AdminUser> sortBy, SortOrder sortOrder, AdminUserSearch search) {
        final String sql =
                " SELECT " +
                "   id, " +
                "   created_at, " +
                "   updated_at, " +
                "   last_password_change, " +
                "   last_login, " +
                "   username, " +
                "   name, " +
                "   email, " +
                "   is_blocked " +
                " FROM admin_users " +
                " WHERE :search IS NULL OR name ILIKE '%'||:search||'%' OR email ILIKE '%'||:search||'%' ";
        return db.getPage(AdminUser.class, sql, page, sortBy, sortOrder, new Parameters(search));
    }

    @Override
    public AdminUser findById(UUID id) {
        final String sql =
                " SELECT " +
                "   id, " +
                "   created_at, " +
                "   updated_at, " +
                "   last_password_change, " +
                "   last_login, " +
                "   username, " +
                "   name, " +
                "   email, " +
                "   is_blocked " +
                " FROM admin_users " +
                " WHERE id = :id ";
        return db.getBean(AdminUser.class, sql, set("id", id));
    }

    @Override
    public UUID insert(AdminUser admin) {
        final String sql =
                " INSERT INTO admin_users (username, password, name, email) " +
                "   VALUES (:username, :encrypted, :name, :email) ";
        return db.insert(sql, admin, set("encrypted", encrypt(admin.getPassword()))).getId();
    }

    @Override
    public void update(AdminUser admin) {
        final String sql =
                " UPDATE admin_users SET " +
                "   username = :username, " +
                "   name = :name, " +
                "   email = :email, " +
                "   updated_at = (CURRENT_TIMESTAMP AT TIME ZONE 'UTC') " +
                "   WHERE id = :id ";
        db.update(sql, admin);
    }

    @Override
    public void changePasswordById(UUID id, String oldPassword, String newPassword) {
        final String sql =
                " UPDATE admin_users SET " +
                "   password = :new, " +
                "   last_password_change = (CURRENT_TIMESTAMP AT TIME ZONE 'UTC') " +
                " WHERE id = :id AND password = :old";
        db.update(sql, set("id", id), set("new", encrypt(newPassword)), set("old", encrypt(oldPassword)));
    }

    @Override
    public void resetPasswordById(UUID id, String newPassword) {
        final String sql =
                " UPDATE admin_users SET " +
                "   password = :new, " +
                "   last_password_change = (CURRENT_TIMESTAMP AT TIME ZONE 'UTC') " +
                " WHERE id = :id";
        db.update(sql, set("id", id), set("new", encrypt(newPassword)));
    }

    @Override
    public void setBlockedById(UUID id, boolean blocked) {
        final String sql = "UPDATE admin_users SET is_blocked = :blocked WHERE id = :id";
        db.update(sql, set("id", id), set("blocked", blocked));
    }

    @Override
    public void deleteById(UUID id) {
        final String sql = "DELETE FROM admin_users WHERE id = :id";
        db.update(sql, set("id", id));
    }

    @Override
    public AdminUser findByUsername(String username) {
        final String sql = "SELECT * FROM admin_users WHERE username = :username AND is_blocked IS FALSE";
        return db.safe(AdminUser.class, sql, set("username", username));
    }

    @Override
    public void loginById(UUID id) {
        final String sql =
                " UPDATE admin_users SET " +
                "   last_login = (CURRENT_TIMESTAMP AT TIME ZONE 'UTC') " +
                " WHERE id = :id AND is_blocked IS FALSE ";
        db.update(sql, set("id", id));
    }
}
