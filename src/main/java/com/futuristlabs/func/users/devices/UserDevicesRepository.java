package com.futuristlabs.func.users.devices;


import java.util.List;
import java.util.UUID;

public interface UserDevicesRepository {
    void register(final UUID userId, final UserDevice device);
    List<UserDevice> getByUserId(final UUID userId);
    void deleteById(final UUID installationId);
}
