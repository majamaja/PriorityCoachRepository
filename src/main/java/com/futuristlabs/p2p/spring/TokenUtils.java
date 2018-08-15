package com.futuristlabs.p2p.spring;

import com.futuristlabs.p2p.func.auth.SessionUser;

public interface TokenUtils {
    String getToken(SessionUser user);

    boolean validate(String token);

    SessionUser getUserFromToken(String token);
}
