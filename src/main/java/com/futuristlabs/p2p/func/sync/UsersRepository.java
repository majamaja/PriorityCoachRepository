package com.futuristlabs.p2p.func.sync;

import com.futuristlabs.p2p.func.auth.AuthenticationRequest;
import com.futuristlabs.p2p.func.auth.Device;
import com.futuristlabs.p2p.func.auth.SessionUser;

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

    void updatePassword(String email, String newPassword);
}
