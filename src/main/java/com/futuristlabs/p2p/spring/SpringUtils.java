package com.futuristlabs.p2p.spring;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.Collections;

public class SpringUtils {

    public static void authenticateUser(UserDetails userDetails, ServletRequest request) {
        final String username = userDetails.getUsername();
        final String password = userDetails.getPassword();
        final Collection<SimpleGrantedAuthority> roles = Collections.singleton(new SimpleGrantedAuthority("ROLE_USER"));
        final WebAuthenticationDetails details = new WebAuthenticationDetailsSource().buildDetails((HttpServletRequest) request);

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(username, password, roles);
        authentication.setDetails(details);

        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
