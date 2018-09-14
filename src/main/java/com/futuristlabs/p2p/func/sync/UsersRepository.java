package com.futuristlabs.p2p.func.sync;

import com.futuristlabs.p2p.func.auth.AuthenticationRequest;
import com.futuristlabs.p2p.func.auth.Device;
import com.futuristlabs.p2p.func.auth.SessionUser;
import com.futuristlabs.p2p.func.userprofile.ChangePasswordRequest;
import com.futuristlabs.p2p.func.userprofile.UserProfile;

import java.util.UUID;

public interface UsersRepository {

    boolean existsByEmail(String email);

    SessionUser findUserByCredentials(AuthenticationRequest authenticationRequest);

    SessionUser findUserByFacebook(AuthenticationRequest authenticationRequest);

    SessionUser findUserByGooglePlus(AuthenticationRequest authenticationRequest);

    SessionUser createWithNative(AuthenticationRequest authenticationRequest);

    SessionUser createWithFacebook(AuthenticationRequest authenticationRequest);

    SessionUser createWithGooglePlus(AuthenticationRequest authenticationRequest);

    void registerUserDevice(UUID userId, Device device);

    void setPassword(String email, String newPassword);

    void updatePassword(ChangePasswordRequest request);

    UserProfile findProfileById(UUID userId);

    void updateProfile(UserProfile userProfile);
}
