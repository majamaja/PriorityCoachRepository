package com.futuristlabs.p2p.func.auth;

public class FacebookLoginRequest {

    private String userId;
    private String tokenId;

    public FacebookLoginRequest() {
    }

    public FacebookLoginRequest(String userId, String tokenId) {
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
