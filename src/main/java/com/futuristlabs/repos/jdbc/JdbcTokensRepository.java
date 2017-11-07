package com.futuristlabs.repos.jdbc;

import com.futuristlabs.func.users.tokens.Token;
import com.futuristlabs.func.users.tokens.TokensRepository;
import com.futuristlabs.repos.jdbc.common.AbstractJdbcRepository;
import org.springframework.stereotype.Repository;


@Repository
public class JdbcTokensRepository extends AbstractJdbcRepository implements TokensRepository {
    @Override
    public void create(Token token) {
        final String sql =
                " INSERT INTO tokens (user_id, token, type, expiry_date, is_used) " +
                "     VALUES (:userId, :token, :type, :expiryDate AT TIME ZONE 'utc', :isUsed) ";
        db.insert(sql, token);
    }

    @Override
    public void delete(String token) {
        final String sql = "DELETE FROM tokens WHERE token = :token";
        db.update(sql, set("token", token));
    }

    @Override
    public Token find(String token) {
        final String sql = "SELECT token, user_id, type, expiry_date, is_used FROM tokens WHERE token = :token";
        return db.getBean(Token.class, sql, set("token", token));
    }

    @Override
    public boolean use(String token) {
        final String sql =
                " UPDATE tokens SET is_used = true " +
                " WHERE token = :token AND is_used = FALSE AND expiry_date > now() AT TIME ZONE 'utc' ";
        return db.update(sql, set("token", token)) >= 1;
    }

    @Override
    public void cleanupUsedAndExpired() {
        db.update("DELETE FROM tokens WHERE is_used OR expiry_date < now() AT TIME ZONE 'utc'");
    }
}
