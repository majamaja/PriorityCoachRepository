package com.futuristlabs.p2p.spring;

import com.futuristlabs.p2p.func.auth.SessionUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.codec.Base64;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
public class AuthenticationTokenProcessingFilter extends GenericFilterBean {

    private TokenUtils tokenUtils;

    @Autowired
    public AuthenticationTokenProcessingFilter(TokenUtils tokenUtils) {
        this.tokenUtils = tokenUtils;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        final String token = getToken((HttpServletRequest) request);

        if (tokenUtils.validate(token)) {
            SessionUser userDetails = tokenUtils.getUserFromToken(token);
            SpringUtils.authenticateUser(userDetails, request);

            request = updateRequest((HttpServletRequest) request, "userId", userDetails.getId().toString());
        }

        chain.doFilter(request, response);
    }

    private HttpServletRequest updateRequest(final HttpServletRequest request, final String parameter, final String value) {
        return new HttpServletRequestWrapper(request) {
            @Override
            public String getParameter(String name) {
                if (parameter.equals(name)) {
                    return value;
                }
                return super.getParameter(name);
            }

            @Override
            public String[] getParameterValues(String name) {
                if (parameter.equals(name)) {
                    return new String[]{ value};
                }
                return super.getParameterValues(name);
            }

            @Override
            public Map getParameterMap() {
                Map paramMap = new HashMap(super.getParameterMap());
                paramMap.put(parameter, value);
                return paramMap;
            }
        };
    }

    private String getToken(HttpServletRequest request) {
        final String authHeader = request.getHeader("Authorization");
        return parseAuthenticationHeader(authHeader);
    }

    private String parseAuthenticationHeader(String headerValue) {
        if (headerValue == null) {
            return null;
        }

        try {
            String base64Token = headerValue.replaceFirst("Basic: ", "");
            return new String(Base64.decode(base64Token.getBytes())).trim();
        } catch (Exception e) {
            return null;
        }
    }
}
