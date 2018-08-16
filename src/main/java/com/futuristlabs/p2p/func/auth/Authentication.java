package com.futuristlabs.p2p.func.auth;

import com.futuristlabs.p2p.emails.MailNotifier;
import com.futuristlabs.p2p.func.sync.UsersRepository;
import com.futuristlabs.p2p.utils.Security;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collection;

@Component
public class Authentication {

    private UsersRepository usersRepository;
    private MailNotifier mailNotifier;
    private AuthenticationManager authenticationManager;

    @Autowired
    public Authentication(UsersRepository usersRepository, MailNotifier mailNotifier, AuthenticationManager authenticationManager) {
        this.usersRepository = usersRepository;
        this.mailNotifier = mailNotifier;
        this.authenticationManager = authenticationManager;
    }

    public SessionUser register(AuthenticationRequest authenticationRequest) {
        if (authenticationRequest.getName() == null) {
            authenticationRequest.setName(authenticationRequest.getEmail());
        }

        if (authenticationRequest.isFacebook() || authenticationRequest.isGPlus()) {
            return login(authenticationRequest);
        }

        if (userExists(authenticationRequest)) {
            return null;
        }

        final SessionUser user = usersRepository.createWithNative(authenticationRequest);
        usersRepository.registerUserDevice(user.getId(), authenticationRequest.getDevice());

        return user;
    }

    public SessionUser login(AuthenticationRequest authenticationRequest) {
        final SessionUser user = findOrCreateUser(authenticationRequest);

        // User not authenticated
        if (user == null) {
            return null;
        }

        usersRepository.registerUserDevice(user.getId(), authenticationRequest.getDevice());

        // Generate session
        final org.springframework.security.core.Authentication auth = authenticationManager.authenticate(new SpringUser(user));
        final SecurityContext securityContext = SecurityContextHolder.getContext();
        securityContext.setAuthentication(auth);

        return user;
    }

    private SessionUser findOrCreateUser(AuthenticationRequest authenticationRequest) {
        final boolean userExists = usersRepository.existsByEmail(authenticationRequest.getEmail());

        if (userExists) {
            return loginExistingUser(authenticationRequest);
        } else {
            return registerOnTheFly(authenticationRequest);
        }
    }

    private SessionUser loginExistingUser(AuthenticationRequest authenticationRequest) {
        if (authenticationRequest.isFacebook()) {
            return usersRepository.findUserByFacebook(authenticationRequest);
        }

        if (authenticationRequest.isGPlus()) {
            return usersRepository.findUserByGooglePlus(authenticationRequest);
        }

        return usersRepository.findUserByCredentials(authenticationRequest);
    }

    private SessionUser registerOnTheFly(AuthenticationRequest authenticationRequest) {
        if (authenticationRequest.getName() == null) {
            authenticationRequest.setName(authenticationRequest.getEmail());
        }

        if (authenticationRequest.isFacebook()) {
            return usersRepository.createWithFacebook(authenticationRequest);
        }

        if (authenticationRequest.isGPlus()) {
            return usersRepository.createWithGooglePlus(authenticationRequest);
        }

        return null;
    }

    private boolean userExists(AuthenticationRequest authenticationRequest) {
        return usersRepository.existsByEmail(authenticationRequest.getEmail());
    }

    public boolean resetPassword(ResetPasswordRequest resetPasswordRequest) {
        final String email = resetPasswordRequest.getEmail();

        if (!usersRepository.existsByEmail(email)) {
            return false;
        }

        final String newPassword = Security.generatePassword();
        usersRepository.updatePassword(email, newPassword);
        mailNotifier.sendNewPasswordEmail(email, newPassword);
        return true;
    }

    public static class SpringUser implements org.springframework.security.core.Authentication {

        private SessionUser user;

        public SpringUser(final SessionUser user) {
            this.user = user;
        }

        @Override
        public Collection<? extends GrantedAuthority> getAuthorities() {
            return Arrays.asList(new SimpleGrantedAuthority("ROLE_USER"));
        }

        @Override
        public Object getCredentials() {
            return user;
        }

        @Override
        public Object getDetails() {
            return user;
        }

        @Override
        public Object getPrincipal() {
            return user;
        }

        @Override
        public boolean isAuthenticated() {
            return user != null;
        }

        @Override
        public void setAuthenticated(final boolean isAuthenticated) throws IllegalArgumentException {
            if (!isAuthenticated) {
                user = null;
            }
        }

        @Override
        public String getName() {
            return user.getUsername();
        }
    }
}
