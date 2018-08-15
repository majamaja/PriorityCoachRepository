package com.futuristlabs.p2p.func.auth;

public class AuthenticationRequest {

    private String email;
    private String password;
    private String name;
    private FacebookLoginRequest facebook;
    private GooglePLusLoginRequest gplus;
    private Device device;

    public String getFbId() {
        if (facebook == null) {
            return null;
        }
        return facebook.getUserId();
    }

    public void setFbId(String fbId) {
        if (facebook == null) {
            facebook = new FacebookLoginRequest();
        }
        facebook.setUserId(fbId);
    }

    public String getGplusId() {
        if (gplus == null) {
            return null;
        }
        return gplus.getUserId();
    }

    public void setGplusId(String gplusId) {
        if (gplus == null) {
            gplus = new GooglePLusLoginRequest();
        }
        gplus.setUserId(gplusId);
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public FacebookLoginRequest getFacebook() {
        return facebook;
    }

    public GooglePLusLoginRequest getGplus() {
        return gplus;
    }

    public void setGplus(GooglePLusLoginRequest gplus) {
        this.gplus = gplus;
    }

    public Device getDevice() {
        return device;
    }

    public void setDevice(Device device) {
        this.device = device;
    }

    public boolean isNative() {
        return password != null;
    }

    public boolean isFacebook() {
        return facebook != null;
    }

    public void setFacebook(FacebookLoginRequest facebook) {
        this.facebook = facebook;
    }

    public boolean isGPlus() {
        return gplus != null;
    }
}
