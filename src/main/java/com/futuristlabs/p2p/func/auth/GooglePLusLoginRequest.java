package com.futuristlabs.p2p.func.auth;

public class GooglePLusLoginRequest {

    private String userId;
    private String tokenId;

    public GooglePLusLoginRequest() {
    }

    public GooglePLusLoginRequest(String userId, String tokenId) {
        this.userId = userId;
        this.tokenId = tokenId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getTokenId() {
        return tokenId;
    }

    public void setTokenId(String tokenId) {
        this.tokenId = tokenId;
    }
}
