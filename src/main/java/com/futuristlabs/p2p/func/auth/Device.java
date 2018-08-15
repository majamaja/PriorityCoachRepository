package com.futuristlabs.p2p.func.auth;

public class Device {

    private String udid;
    private String type;
    private String apnToken;

    public String getUdid() {
        return udid;
    }

    public void setUdid(String udid) {
        this.udid = udid;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getApnToken() {
        return apnToken;
    }

    public void setApnToken(String apnToken) {
        this.apnToken = apnToken;
    }
}
