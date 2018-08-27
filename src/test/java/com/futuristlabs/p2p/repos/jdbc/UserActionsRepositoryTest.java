package com.futuristlabs.p2p.repos.jdbc;

import com.futuristlabs.p2p.func.useractions.UserActionsLog;
import com.futuristlabs.p2p.func.useractions.UserActionsRepository;
import com.futuristlabs.p2p.repos.RepositoryTest;
import org.joda.time.DateTime;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class UserActionsRepositoryTest extends RepositoryTest {

    @Autowired
    private UserActionsRepository repo;

    @Test
    public void modifiedActionsLogs() {
        final List<UserActionsLog> actionLogs = repo.modifiedActionsLogs(UUID.randomUUID(), new DateTime());
        assertTrue(actionLogs.isEmpty());
    }

    @Test
    public void modifiedActionsLogs_NoDate() {
        final List<UserActionsLog> actionLogs = repo.modifiedActionsLogs(UUID.randomUUID(), null);
        assertTrue(actionLogs.isEmpty());
    }

    @Test
    public void modifiedActionsLogs_WithData() {
        final UUID user = sampleData.user();
        final UUID action = sampleData.lifeUpgradeActionWithUser(user);
        sampleData.actionLog(user, action);

        final List<UserActionsLog> actionLogs = repo.modifiedActionsLogs(user, null);
        assertEquals(action, actionLogs.get(0).getLifeUpgradeActionId());
    }

    @Test
    public void modifiedActionsLogsRestricted_WithData() {
        final UUID friendA = sampleData.user();
        final UUID friendB = sampleData.user();
        final UUID friendship = sampleData.friendship(friendA, friendB);

        final UUID actionFriendA = sampleData.lifeUpgradeActionWithUser(friendA);
        sampleData.actionLog(friendA, actionFriendA);

        sampleData.permissions(friendA, friendship, actionFriendA, true);

        final List<UserActionsLog> friendActionLogs = repo.modifiedActionsLogsRestricted(friendA, null, friendB);
        assertEquals(actionFriendA, friendActionLogs.get(0).getLifeUpgradeActionId());
    }

    @Test
    public void modifiedActionsLogsRestricted_ActionNotShared() {
        final UUID friendA = sampleData.user();
        final UUID friendB = sampleData.user();
        final UUID friendship = sampleData.friendship(friendA, friendB);

        final UUID actionFriendA = sampleData.lifeUpgradeActionWithUser(friendA);
        sampleData.actionLog(friendA, actionFriendA);

        final List<UserActionsLog> friendActionLogs = repo.modifiedActionsLogsRestricted(friendA, null, friendB);
        assertTrue(friendActionLogs.isEmpty());
    }

    @Test
    public void deletedActionsLogs() {
        final List<UUID> uuids = repo.deletedActionsLogs(UUID.randomUUID(), new DateTime());
        assertTrue(uuids.isEmpty());
    }

    @Test
    public void deletedActionsLogsNoDate() {
        final List<UUID> uuids = repo.deletedActionsLogs(UUID.randomUUID(), null);
        assertTrue(uuids.isEmpty());
    }

    @Test
    public void modifyActionsLogs() {
        final UUID userId = sampleData.user();
        final UUID lifeUpgradeAction = sampleData.lifeUpgradeActionWithUser(userId);

        final UserActionsLog userActionsLog = new UserActionsLog();
        userActionsLog.setId(UUID.randomUUID());
        userActionsLog.setActionDateAsDate(new DateTime());
        userActionsLog.setTimesDone(3);
        userActionsLog.setLifeUpgradeActionId(lifeUpgradeAction);

        repo.modifyActionsLogs(userId, Arrays.asList(userActionsLog));

        userActionsLog.setTimesDone(userActionsLog.getTimesDone() + 1);
        repo.modifyActionsLogs(userId, Arrays.asList(userActionsLog));
    }

    @Test
    public void deleteActionsLogs() {
        repo.deleteActionsLogs(UUID.randomUUID(), Arrays.asList(UUID.randomUUID()));
    }

    @Test
    public void deleteActionsLogsNoData() {
        repo.deleteActionsLogs(UUID.randomUUID(), new ArrayList<UUID>());
    }

    @Test
    public void deleteActionsLogsNoDataNull() {
        repo.deleteActionsLogs(UUID.randomUUID(), null);
    }

}
