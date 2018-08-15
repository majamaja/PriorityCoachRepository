package com.futuristlabs.p2p.spring;

import org.springframework.session.Session;
import org.springframework.session.web.http.HeaderHttpSessionStrategy;
import org.springframework.session.web.http.HttpSessionStrategy;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class RequestOrHeaderHttpSessionStrategyAdapter implements HttpSessionStrategy {

    private HeaderHttpSessionStrategy headerSessionStrategy = new HeaderHttpSessionStrategy();
    private static final String X_AUTH_TOKEN = "x-auth-token";

    @Override
    public String getRequestedSessionId(HttpServletRequest request) {
        final String sessionTokenFromHeader = headerSessionStrategy.getRequestedSessionId(request);
        return null != sessionTokenFromHeader ? sessionTokenFromHeader : sessionTokenFromRequest(request);
    }

    private String sessionTokenFromRequest(HttpServletRequest request) {
        return request.getParameter(X_AUTH_TOKEN);
    }

    @Override
    public void onNewSession(Session session, HttpServletRequest request, HttpServletResponse response) {
        headerSessionStrategy.onNewSession(session, request, response);
    }

    @Override
    public void onInvalidateSession(HttpServletRequest request, HttpServletResponse response) {
        headerSessionStrategy.onInvalidateSession(request, response);
    }
}
