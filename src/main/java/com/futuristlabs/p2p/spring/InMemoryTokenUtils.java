package com.futuristlabs.p2p.spring;

import com.futuristlabs.p2p.func.auth.SessionUser;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Scope(value = "singleton")
@Component
public class InMemoryTokenUtils implements TokenUtils {
    private Map<String, SessionUser> tokens = new HashMap<>();

    @Override
    public String getToken(SessionUser user) {
        String token = UUID.randomUUID().toString();
        tokens.put(token, user);
        return token;
    }

    @Override
    public boolean validate(String token) {
        return tokens.containsKey(token);
    }

    @Override
    public SessionUser getUserFromToken(String token) {
        return tokens.get(token);
    }
}
