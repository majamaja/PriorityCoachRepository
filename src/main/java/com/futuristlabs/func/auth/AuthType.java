package com.futuristlabs.func.auth;

public enum AuthType {
    ADMIN("A"),
    USER("U");

    private final String token;

    AuthType(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }
}

