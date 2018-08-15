package com.futuristlabs.p2p.func.auth;

import java.util.UUID;

public class UserSession {

    private UUID userId;
    private String accessToken;

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getAccessToken() {
        return accessToken;
    }
}
