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

import static org.junit.Assert.assertTrue;

public class UserActionsRepositoryTest extends RepositoryTest {

    @Autowired
    private UserActionsRepository repo;

    @Test
    public void modifiedActionsLogs() {
        final List<UserActionsLog> categories = repo.modifiedActionsLogs(UUID.randomUUID(), new DateTime());
        assertTrue(categories.isEmpty());
    }

    @Test
    public void modifiedActionsLogsNoDate() {
        final List<UserActionsLog> categories = repo.modifiedActionsLogs(UUID.randomUUID(), null);
        assertTrue(categories.isEmpty());
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
