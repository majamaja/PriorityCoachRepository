package com.futuristlabs.p2p.repos.jdbc;

import com.futuristlabs.p2p.func.auth.*;
import com.futuristlabs.p2p.func.sync.UsersRepository;
import com.futuristlabs.p2p.repos.RepositoryTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.*;

public class UsersRepositoryTest extends RepositoryTest {

    @Autowired
    private UsersRepository repo;

    @Test
    public void noDataNoUserExists() {
        assertFalse(repo.existsByEmail("test@example.com"));
    }

    @Test
    public void registeredUserExists() {
        final Device device = new Device();
        device.setUdid("__UDID__");
        device.setType("ios");
        device.setApnToken("__APN.TOKEN__");

        AuthenticationRequest registration = new AuthenticationRequest();
        registration.setEmail("user.x@example.com");
        registration.setPassword("password123");
        registration.setName("User X");
        registration.setDevice(device);

        repo.createWithNative(registration);
        assertTrue(repo.existsByEmail(registration.getEmail()));
    }

    @Test
    public void registeredNativeUserExistsPassword() {
        final Device device = new Device();
        device.setUdid("__UDID__");
        device.setType("ios");
        device.setApnToken("__APN.TOKEN__");

        final String email = "user.x@example.com";
        final String password = "password123";

        AuthenticationRequest registration = new AuthenticationRequest();
        registration.setEmail(email);
        registration.setPassword(password);
        registration.setName("User X");
        registration.setDevice(device);

        final SessionUser user = repo.createWithNative(registration);
        final AuthenticationRequest authenticationRequest = new AuthenticationRequest();
        authenticationRequest.setEmail(email);
        authenticationRequest.setPassword(password);

        final SessionUser sessionUser = repo.findUserByCredentials(authenticationRequest);
        assertNotNull(sessionUser);
        assertEquals(user.getId(), sessionUser.getId());
        assertEquals(email, sessionUser.getUsername());
    }

    @Test
    public void findUserByCredentials() {
        final AuthenticationRequest authenticationRequest = new AuthenticationRequest();
        authenticationRequest.setEmail("test@example.com");
        authenticationRequest.setPassword("123456");
        assertNull(repo.findUserByCredentials(authenticationRequest));
    }

    @Test
    public void findUserByCredentialsWithMissingData() {
        final AuthenticationRequest authenticationRequest = new AuthenticationRequest();
        authenticationRequest.setEmail(null);
        authenticationRequest.setPassword(null);
        assertNull(repo.findUserByCredentials(authenticationRequest));
    }

    @Test
    public void findUserByFacebook() {
        final AuthenticationRequest authenticationRequest = new AuthenticationRequest();
        authenticationRequest.setEmail("test.fb@example.com");
        authenticationRequest.setFacebook(new FacebookLoginRequest("u1234", "t1234"));
        assertNull(repo.findUserByFacebook(authenticationRequest));
    }

    @Test
    public void findUserByFacebookWithMissingData() {
        assertNull(repo.findUserByFacebook(new AuthenticationRequest()));

        final AuthenticationRequest authenticationRequest = new AuthenticationRequest();
        authenticationRequest.setFacebook(new FacebookLoginRequest());
        assertNull(repo.findUserByFacebook(authenticationRequest));
    }

    @Test
    public void findUserByGooglePlus() {
        final AuthenticationRequest authenticationRequest = new AuthenticationRequest();
        authenticationRequest.setGplus(new GooglePLusLoginRequest("u1234", "t1234"));
        assertNull(repo.findUserByGooglePlus(authenticationRequest));
    }

    @Test
    public void findUserByGooglePlusWithMissingData() {
        assertNull(repo.findUserByGooglePlus(new AuthenticationRequest()));

        final AuthenticationRequest authenticationRequest = new AuthenticationRequest();
        authenticationRequest.setGplus(new GooglePLusLoginRequest());
        assertNull(repo.findUserByGooglePlus(authenticationRequest));
    }

    @Test
    public void createWithFacebook() {
        final AuthenticationRequest authenticationRequest = new AuthenticationRequest();
        authenticationRequest.setFacebook(new FacebookLoginRequest("user13", "token42"));
        authenticationRequest.setEmail("test.fb@example.com");
        authenticationRequest.setName("User F");
        assertNotNull(repo.createWithFacebook(authenticationRequest));
    }

    @Test
    public void createWithGooglePlus() {
        final AuthenticationRequest authenticationRequest = new AuthenticationRequest();
        authenticationRequest.setGplus(new GooglePLusLoginRequest("user42", "token13"));
        authenticationRequest.setEmail("test.gplus@example.com");
        authenticationRequest.setName("User G");
        assertNotNull(repo.createWithGooglePlus(authenticationRequest));
    }

    @Test
    public void changePassword() {
        repo.updatePassword("test@example.com", "Secret");
    }
}
