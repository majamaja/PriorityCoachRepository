package com.futuristlabs.p2p.func.useractions;

import org.joda.time.DateTime;

import java.util.List;
import java.util.UUID;

public interface UserActionsRepository {
    List<UserActionsLog> modifiedActionsLogs(UUID userId, DateTime modifiedSince);

    /**
     Filter the list by permissions, granted from user to friend
     */
    List<UserActionsLog> modifiedActionsLogsRestricted(UUID userId, DateTime modifiedSince, UUID friendId);

    List<UUID> deletedActionsLogs(UUID userId, DateTime modifiedSince);

    void modifyActionsLogs(UUID userId, List<UserActionsLog> userActionsLogs);

    void deleteActionsLogs(UUID userId, List<UUID> userActionsLogs);

}
