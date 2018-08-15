package com.futuristlabs.p2p.func.auth;

import com.futuristlabs.p2p.emails.MailNotifier;
import com.futuristlabs.p2p.func.sync.UsersRepository;
import com.futuristlabs.p2p.spring.TokenUtils;
import com.futuristlabs.p2p.utils.Security;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Authentication {
    private UsersRepository usersRepository;
    private TokenUtils tokenUtils;
    private MailNotifier mailNotifier;

    @Autowired
    public Authentication(UsersRepository usersRepository, TokenUtils tokenUtils, MailNotifier mailNotifier) {
        this.usersRepository = usersRepository;
        this.tokenUtils = tokenUtils;
        this.mailNotifier = mailNotifier;
    }

    public UserSession register(AuthenticationRequest authenticationRequest) {
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
        final String token = tokenUtils.getToken(new SessionUser(user.getId(), user.getUsername()));

        usersRepository.registerUserDevice(user.getId(), authenticationRequest.getDevice());

        final UserSession userSession = new UserSession();
        userSession.setUserId(user.getId());
        userSession.setAccessToken(token);
        return userSession;
    }

    public UserSession login(AuthenticationRequest authenticationRequest) {
        final SessionUser user = findOrCreateUser(authenticationRequest);

        if (user == null) {
            return null;
        }

        final String token = tokenUtils.getToken(user);

        usersRepository.registerUserDevice(user.getId(), authenticationRequest.getDevice());

        final UserSession userSession = new UserSession();
        userSession.setUserId(user.getId());
        userSession.setAccessToken(token);
        return userSession;
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
}
