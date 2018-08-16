package com.futuristlabs.p2p.func;

import com.futuristlabs.p2p.emails.MailNotifier;
import com.futuristlabs.p2p.func.auth.*;
import com.futuristlabs.p2p.func.sync.UsersRepository;
import org.junit.Test;
import org.springframework.security.authentication.AuthenticationManager;

import java.util.UUID;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class AuthenticationTest {

    private Authentication auth;

    @Test
    public void registerNewUser() {
        final SessionUser sessionUser = new SessionUser();

        final UsersRepository userRepo = mock(UsersRepository.class);
        final MailNotifier mailNotifier = mock(MailNotifier.class);
        final AuthenticationManager authManager = mock(AuthenticationManager.class);
        auth = new Authentication(userRepo, mailNotifier, authManager);

        when(userRepo.existsByEmail(anyString())).thenReturn(false);
        when(userRepo.createWithNative(any(AuthenticationRequest.class))).thenReturn(sessionUser);

        final AuthenticationRequest reg = new AuthenticationRequest();
        reg.setEmail("new-user@example.com");
        reg.setPassword("P@ssw0rd");

        assertEquals(sessionUser.getId(), auth.register(reg).getId());
    }

    @Test
    public void registerExistingUser() {
        final UsersRepository userRepo = mock(UsersRepository.class);
        when(userRepo.existsByEmail(anyString())).thenReturn(true);

        auth = new Authentication(userRepo, null, null);

        assertNull(auth.register(new AuthenticationRequest()));
    }

    @Test
    public void loginWithEmail() {
        final UUID userId = UUID.randomUUID();

        final UsersRepository userRepo = mock(UsersRepository.class);
        final MailNotifier mailNotifier = mock(MailNotifier.class);
        final AuthenticationManager authManager = mock(AuthenticationManager.class);
        auth = new Authentication(userRepo, mailNotifier, authManager);

        when(userRepo.existsByEmail(anyString())).thenReturn(true);
        when(userRepo.findUserByCredentials(any(AuthenticationRequest.class))).thenReturn(new SessionUser(userId, "test@example.com"));

        final AuthenticationRequest authenticationRequest = new AuthenticationRequest();
        final SessionUser user = auth.login(authenticationRequest);
        assertNotNull(user);
        assertEquals(userId, user.getId());
    }

    @Test
    public void loginWithEmailWrongPass() {
        final UsersRepository userRepo = mock(UsersRepository.class);
        final MailNotifier mailNotifier = mock(MailNotifier.class);
        final AuthenticationManager authManager = mock(AuthenticationManager.class);
        auth = new Authentication(userRepo, mailNotifier, authManager);

        when(userRepo.findUserByCredentials(any(AuthenticationRequest.class))).thenReturn(null);

        assertNull(auth.login(new AuthenticationRequest()));
    }

    @Test
    public void loginWithFacebook() {
        final String USER_EMAIL = "test.fb@example.com";
        final SessionUser sessionUser = new SessionUser();

        final AuthenticationRequest authenticationRequest = new AuthenticationRequest();
        authenticationRequest.setFacebook(new FacebookLoginRequest("FB4212", "FB:Token:1241"));
        authenticationRequest.setEmail(USER_EMAIL);

        final UsersRepository userRepo = mock(UsersRepository.class);
        final MailNotifier mailNotifier = mock(MailNotifier.class);
        final AuthenticationManager authManager = mock(AuthenticationManager.class);
        auth = new Authentication(userRepo, mailNotifier, authManager);

        when(userRepo.existsByEmail(anyString())).thenReturn(true);
        when(userRepo.findUserByFacebook(any(AuthenticationRequest.class))).thenReturn(sessionUser);

        assertEquals(sessionUser.getId(), auth.login(authenticationRequest).getId());
    }

    @Test
    public void loginWithGPlus() {
        final String USER_EMAIL = "test.gplus@example.com";
        final SessionUser sessionUser = new SessionUser();

        final AuthenticationRequest authenticationRequest = new AuthenticationRequest();
        authenticationRequest.setGplus(new GooglePLusLoginRequest("GPlus4212", "GPlus:Token:1241"));
        authenticationRequest.setEmail(USER_EMAIL);

        final UsersRepository userRepo = mock(UsersRepository.class);
        final MailNotifier mailNotifier = mock(MailNotifier.class);
        final AuthenticationManager authManager = mock(AuthenticationManager.class);
        auth = new Authentication(userRepo, mailNotifier, authManager);

        when(userRepo.existsByEmail(anyString())).thenReturn(true);
        when(userRepo.findUserByGooglePlus(any(AuthenticationRequest.class))).thenReturn(sessionUser);

        assertEquals(sessionUser.getId(), auth.login(authenticationRequest).getId());
    }

}
