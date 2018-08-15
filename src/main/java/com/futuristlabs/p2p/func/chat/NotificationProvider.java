package com.futuristlabs.p2p.func.chat;

import java.util.Collection;

public interface NotificationProvider {
    void notifyAll(final Collection<String> tokens, final String message);
}
