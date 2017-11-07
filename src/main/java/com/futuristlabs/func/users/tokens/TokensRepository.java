package com.futuristlabs.func.users.tokens;

public interface TokensRepository {
    void create(Token token);
    Token find(String token);
    boolean use(String token);
    void delete(String token);
    void cleanupUsedAndExpired();
}
