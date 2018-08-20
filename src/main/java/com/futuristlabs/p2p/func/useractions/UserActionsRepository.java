package com.futuristlabs.p2p.func.useractions;

import org.joda.time.DateTime;

import java.util.List;
import java.util.UUID;

public interface UserActionsRepository {
    List<UserActionsLog> modifiedActionsLogs(UUID userId, DateTime modifiedSince);

    List<UUID> deletedActionsLogs(UUID userId, DateTime modifiedSince);

    void modifyActionsLogs(UUID userId, List<UserActionsLog> userActionsLogs);

    void deleteActionsLogs(UUID userId, List<UUID> userActionsLogs);

}
