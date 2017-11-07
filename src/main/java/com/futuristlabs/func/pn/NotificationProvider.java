package com.futuristlabs.func.pn;

import com.futuristlabs.func.users.devices.UserDevice;

import java.util.List;

public interface NotificationProvider {
    void notifyAll(final String payload, final List<UserDevice> devices, final boolean isDev);
}
