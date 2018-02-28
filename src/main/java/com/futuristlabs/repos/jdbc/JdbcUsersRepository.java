package com.futuristlabs.repos.jdbc;

import com.futuristlabs.func.users.*;
import com.futuristlabs.repos.jdbc.common.AbstractJdbcRepository;
import com.futuristlabs.repos.jdbc.common.InsertResult;
import com.futuristlabs.repos.jdbc.common.Parameters;
import com.futuristlabs.utils.crypto.CryptoUtils;
import com.futuristlabs.utils.repository.Page;
import com.futuristlabs.utils.repository.PageData;
import com.futuristlabs.utils.repository.SortBy;
import com.futuristlabs.utils.repository.SortOrder;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public class JdbcUsersRepository extends AbstractJdbcRepository implements UsersRepository {
    private String encrypt(final String password) {
        return CryptoUtils.encodePassword(password);
    }

    @Override
    public PageData<User> findPaged(Page page, SortBy<User> sortBy, SortOrder sortOrder, UserSearch search) {
        final String sql =
                " SELECT " +
                "   * " +
                " FROM users " +
                " WHERE :search IS NULL " +
                "       OR name ILIKE '%'||:search||'%' " +
                "       OR email ILIKE '%'||:search||'%' ";
        return db.getPage(User.class, sql, page, sortBy, sortOrder, new Parameters(search));
    }

    @Override
    public User findById(UUID userId) {
        final String sql = " SELECT * FROM users WHERE id = :id";
        return db.getBean(User.class, sql, set("id", userId));
    }

    @Override
    public InsertResult insert(User user) {
        final String sql =
                " INSERT INTO users (email, password, name) " +
                "   VALUES (:email, :encoded, :name) ";
        return db.insert(sql, user, set("encoded", encrypt(user.getPassword())));
    }

    @Override
    public void update(User user) {
        final String sql =
                " UPDATE users SET " +
                "   email = :email, " +
                "   name = :name, " +
                "   updated_at = (CURRENT_TIMESTAMP AT TIME ZONE 'UTC') " +
                " WHERE id = :id ";
        db.update(sql, user);
    }

    @Override
    public boolean changePassword(UUID userId, String oldPassword, String newPassword) {
        final String sql =
                " UPDATE users SET " +
                        "   password = :new, " +
                        "   last_password_change = (CURRENT_TIMESTAMP AT TIME ZONE 'UTC') " +
                        " WHERE id = :id AND password = :old";
        final int updated = db.update(sql, set("id", userId), set("old", encrypt(oldPassword)), set("new", encrypt(newPassword)));
        return updated > 0;
    }

    @Override
    public boolean resetPassword(String email, String newPassword) {
        final String sql =
                " UPDATE users SET " +
                        "   password = :new, " +
                        "   last_password_change = (CURRENT_TIMESTAMP AT TIME ZONE 'UTC') " +
                        " WHERE email = :email ";
        final int updated = db.update(sql, set("email", email), set("new", encrypt(newPassword)));
        return updated > 0;
    }

    @Override
    public void deleteById(UUID userId) {
        final String sql = "DELETE FROM users WHERE id = :id";
        db.update(sql, set("id", userId));
    }

    @Override
    public User findByEmail(String email) {
        final String sql = "SELECT * FROM users WHERE email = :email";
        return db.safe(User.class, sql, set("email", email));
    }

    @Override
    public void loginById(UUID userId) {
        final String sql = "UPDATE users SET last_login = now() WHERE id = :id";
        db.update(sql, set("id", userId));
    }
}
