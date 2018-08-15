package com.futuristlabs.p2p.func.useractions;

import org.joda.time.DateTime;

import java.util.List;
import java.util.UUID;

public interface UserActionsRepository {
    List<UserActionItem> modifiedActionItems(UUID userId, DateTime modifiedSince);

    List<UUID> deletedActionItems(UUID userId, DateTime modifiedSince);

    List<UserActionsLog> modifiedActionsLogs(UUID userId, DateTime modifiedSince);

    List<UUID> deletedActionsLogs(UUID userId, DateTime modifiedSince);

    void modifyActionItems(UUID userId, List<UserActionItem> userActionItems);

    void modifyActionsLogs(UUID userId, List<UserActionsLog> userActionsLogs);

    void deleteActionItems(UUID userId, List<UUID> userActionItems);

    void deleteActionsLogs(UUID userId, List<UUID> userActionsLogs);

}
