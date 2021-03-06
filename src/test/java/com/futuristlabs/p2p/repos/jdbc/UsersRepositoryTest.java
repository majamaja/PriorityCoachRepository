package com.futuristlabs.p2p.repos.jdbc;

import com.futuristlabs.p2p.func.auth.*;
import com.futuristlabs.p2p.func.sync.UsersRepository;
import com.futuristlabs.p2p.func.userprofile.ChangePasswordRequest;
import com.futuristlabs.p2p.func.userprofile.UserProfile;
import com.futuristlabs.p2p.repos.RepositoryTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

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
    public void setPassword_nonExistingUser() {
        repo.setPassword("test@example.com", "Secret");
    }

    @Test
    public void updatePassword_nonExistingUser() {
        final ChangePasswordRequest request = new ChangePasswordRequest();
        request.setUserId(UUID.randomUUID());
        request.setOldPassword("Old Pass");
        request.setNewPassword("New Pass");
        repo.updatePassword(request);
    }

    @Test
    public void updatePassword_existingUser() {
        sampleData.enableLog();

        final String email = UUID.randomUUID() + "@example.com";
        final String oldPassword = "Secret";
        final String newPassword = "NewSecret";
        final UUID userId = sampleData.userWithCredentials(email, oldPassword);

        repo.updatePassword(new ChangePasswordRequest(userId, oldPassword, newPassword));

        final AuthenticationRequest authRequestOld = new AuthenticationRequest();
        authRequestOld.setEmail(email);
        authRequestOld.setPassword(oldPassword);
        assertNull(repo.findUserByCredentials(authRequestOld));

        final AuthenticationRequest authRequestNew = new AuthenticationRequest();
        authRequestNew.setEmail(email);
        authRequestNew.setPassword(newPassword);
        assertNotNull(repo.findUserByCredentials(authRequestNew));
    }

    @Test
    public void findProfileById_nonExisting() {
        assertNull(repo.findProfileById(UUID.randomUUID()));
    }

    @Test
    public void findProfileById_Existing() {
        final UUID user = sampleData.user();
        assertNotNull(repo.findProfileById(user));
    }

    @Test
    public void updateProfile_NonExistingUser() {
        final UUID userId = UUID.randomUUID();

        final UserProfile userProfile = new UserProfile();
        userProfile.setId(userId);
        userProfile.setEmail("test@test.com");
        userProfile.setName("Test Test");

        repo.updateProfile(userProfile);
        assertNull(repo.findProfileById(userId));
    }

    @Test
    public void updateProfile_ExistingUser() {
        final UUID userId = sampleData.user();

        final UserProfile userProfile = new UserProfile();
        userProfile.setId(userId);
        userProfile.setEmail(userId + "@existing_user_test.com");
        userProfile.setName("User " + userId);

        repo.updateProfile(userProfile);

        final UserProfile updatedProfile = repo.findProfileById(userId);
        assertEquals(updatedProfile, userProfile);
    }

}
