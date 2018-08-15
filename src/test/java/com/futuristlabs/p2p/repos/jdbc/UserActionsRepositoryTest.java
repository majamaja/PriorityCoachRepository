package com.futuristlabs.p2p.repos.jdbc;

import com.futuristlabs.p2p.func.useractions.UserActionItem;
import com.futuristlabs.p2p.func.useractions.UserActionItemFrequency;
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
    public void modifiedActionItems() {
        final List<UserActionItem> categories = repo.modifiedActionItems(UUID.randomUUID(), new DateTime());
        assertTrue(categories.isEmpty());
    }

    @Test
    public void modifiedActionItemsNoDate() {
        final List<UserActionItem> categories = repo.modifiedActionItems(UUID.randomUUID(), null);
        assertTrue(categories.isEmpty());
    }

    @Test
    public void deletedActionItems() {
        final List<UUID> uuids = repo.deletedActionItems(UUID.randomUUID(), new DateTime());
        assertTrue(uuids.isEmpty());
    }

    @Test
    public void deletedActionItemsNoDate() {
        final List<UUID> uuids = repo.deletedActionItems(UUID.randomUUID(), null);
        assertTrue(uuids.isEmpty());
    }

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
    public void modifyActionItems() {
        final UUID userId = sampleData.user();
        final UUID lifeUpgradeActionId = sampleData.lifeUpgradeAction();

        final UserActionItem userActionItem = new UserActionItem();
        userActionItem.setId(UUID.randomUUID());
        userActionItem.setUserId(userId);
        userActionItem.setActionId(lifeUpgradeActionId);
        userActionItem.setTimesPerDay(2);
        userActionItem.setFrequency(UserActionItemFrequency.DAILY);
        userActionItem.setDayOfWeekMon(true);
        userActionItem.setEveryXDay(2);
        userActionItem.setStartDateAsDate(new DateTime());

        repo.modifyActionItems(userId, Arrays.asList(userActionItem));

        userActionItem.setTimesPerDay(1);
        userActionItem.setFrequency(UserActionItemFrequency.DAILY);
        userActionItem.setDayOfWeekMon(false);
        userActionItem.setDayOfWeekThr(true);
        userActionItem.setEveryXDay(2);
        repo.modifyActionItems(userId, Arrays.asList(userActionItem));
    }

    @Test
    public void modifyActionItemsNoData() {
        repo.modifyActionItems(UUID.randomUUID(), new ArrayList<UserActionItem>());
    }

    @Test
    public void modifyActionItemsNoDataNull() {
        repo.modifyActionItems(UUID.randomUUID(), null);
    }

    @Test
    public void modifyActionsLogs() {
        final UUID userId = sampleData.user();
        final UUID userActionItemId = sampleData.userActionItem(userId);

        final UserActionsLog userActionsLog = new UserActionsLog();
        userActionsLog.setId(UUID.randomUUID());
        userActionsLog.setActionDateAsDate(new DateTime());
        userActionsLog.setTimesDone(3);
        userActionsLog.setUserActionItemId(userActionItemId);

        repo.modifyActionsLogs(userId, Arrays.asList(userActionsLog));

        userActionsLog.setTimesDone(userActionsLog.getTimesDone() + 1);
        repo.modifyActionsLogs(userId, Arrays.asList(userActionsLog));
    }

    @Test
    public void deleteActionItems() {
        repo.deleteActionItems(UUID.randomUUID(), Arrays.asList(UUID.randomUUID()));
    }

    @Test
    public void deleteActionItemsNoData() {
        repo.deleteActionItems(UUID.randomUUID(), new ArrayList<UUID>());
    }

    @Test
    public void deleteActionItemsNoDataNull() {
        repo.deleteActionItems(UUID.randomUUID(), null);
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
