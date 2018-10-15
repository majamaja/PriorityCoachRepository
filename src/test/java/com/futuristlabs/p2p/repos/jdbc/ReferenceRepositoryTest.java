package com.futuristlabs.p2p.repos.jdbc;

import com.futuristlabs.p2p.func.lifeupgrade.LifeUpgradeAction;
import com.futuristlabs.p2p.func.lifeupgrade.LifeUpgradeCategory;
import com.futuristlabs.p2p.func.lifeupgrade.ReferenceRepository;
import com.futuristlabs.p2p.repos.RepositoryTest;
import org.joda.time.DateTime;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ReferenceRepositoryTest extends RepositoryTest {

    @Autowired
    private ReferenceRepository repo;

    @Test
    public void modifiedLifeUpgradeCategories() {
        final List<LifeUpgradeCategory> categories = repo.modifiedLifeUpgradeCategories(new DateTime());
        assertTrue(categories.isEmpty());
    }

    @Test
    public void modifiedLifeUpgradeCategoriesNoDate() {
        repo.modifiedLifeUpgradeCategories(null);
    }

    @Test
    public void deletedLifeUpgradeCategories() {
        final List<UUID> uuids = repo.deletedLifeUpgradeCategories(new DateTime());
        assertTrue(uuids.isEmpty());
    }

    @Test
    public void deletedLifeUpgradeCategories_NoDate() {
        final List<UUID> uuids = repo.deletedLifeUpgradeCategories(null);
        assertTrue(uuids.isEmpty());
    }

    @Test
    public void modifiedUserLifeUpgradeActions() {
        final List<LifeUpgradeAction> actions = repo.modifiedUserLifeUpgradeActions(UUID.randomUUID(), new DateTime());
        assertTrue(actions.isEmpty());
    }

    @Test
    public void modifiedUserLifeUpgradeActions_noDate() {
        final List<LifeUpgradeAction> actions = repo.modifiedUserLifeUpgradeActions(UUID.randomUUID(), null);
        assertTrue(actions.isEmpty());
    }

    @Test
    public void modifiedUserLifeUpgradeActions_WithData() {
        final UUID user = sampleData.user();
        sampleData.lifeUpgradeActionWithUser(user);
        sampleData.lifeUpgradeActionWithUser(user);
        sampleData.lifeUpgradeActionWithUser(user);

        final List<LifeUpgradeAction> actions = repo.modifiedUserLifeUpgradeActions(user, null);
        assertEquals(3, actions.size());
    }

    @Test
    public void modifiedUserLifeUpgradeActionsRestricted_NothingShared() {
        final UUID user = sampleData.user();
        final UUID friend = sampleData.user();
        sampleData.friendship(user, friend);

        final List<LifeUpgradeAction> actions = repo.modifiedUserLifeUpgradeActionsRestricted(user, null, friend);
        assertTrue(actions.isEmpty());
    }

    @Test
    public void modifiedUserLifeUpgradeActionsRestricted_WithSharedItem() {
        final UUID user = sampleData.user();
        final UUID friend = sampleData.user();
        final UUID friendship = sampleData.friendship(user, friend);

        final UUID action = sampleData.lifeUpgradeActionWithUser(friend);
        sampleData.permissions(friend, friendship, action, true);

        final UUID sharedActionId = repo.modifiedUserLifeUpgradeActionsRestricted(friend, null, user).get(0).getId();
        assertEquals(action, sharedActionId);
    }

    @Test
    public void modifiedUserLifeUpgradeActionsRestricted_WithForbiddenItem() {
        final UUID user = sampleData.user();
        final UUID friend = sampleData.user();
        final UUID friendship = sampleData.friendship(user, friend);

        final UUID action = sampleData.lifeUpgradeActionWithUser(friend);
        sampleData.permissions(friend, friendship, action, false);

        final List<LifeUpgradeAction> actions = repo.modifiedUserLifeUpgradeActionsRestricted(friend, null, user);
        assertTrue(actions.isEmpty());
    }

    @Test
    public void deletedUserLifeUpgradeActions() {
        final List<UUID> uuids = repo.deletedUserLifeUpgradeActions(UUID.randomUUID(), new DateTime());
        assertTrue(uuids.isEmpty());
    }

    @Test
    public void deletedUserLifeUpgradeActions_NoDate() {
        final List<UUID> uuids = repo.deletedUserLifeUpgradeActions(UUID.randomUUID(), null);
        assertTrue(uuids.isEmpty());
    }

    @Test
    public void modifyUserLifeUpgradeActions() {
        final UUID lifeUpgradeCategory = sampleData.lifeUpgradeCategory();
        final UUID userId = sampleData.user();

        final LifeUpgradeAction lifeUpgradeAction = new LifeUpgradeAction();
        lifeUpgradeAction.setId(UUID.randomUUID());
        lifeUpgradeAction.setLifeUpgradeCategoryId(lifeUpgradeCategory);
        lifeUpgradeAction.setName("Test " + UUID.randomUUID());

        repo.modifyUserLifeUpgradeActions(userId, Arrays.asList(lifeUpgradeAction));
        repo.deleteUserLifeUpgradeActions(userId, Arrays.asList(lifeUpgradeAction.getId()));
    }

    @Test
    public void modifyUserLifeUpgradeActions_NoActions() {
        LifeUpgradeAction lifeUpgradeAction = new LifeUpgradeAction();
        lifeUpgradeAction.setId(UUID.randomUUID());
        lifeUpgradeAction.setLifeUpgradeCategoryId(UUID.randomUUID());
        lifeUpgradeAction.setName("Test");

        repo.modifyUserLifeUpgradeActions(UUID.randomUUID(), new ArrayList<LifeUpgradeAction>());
    }

    @Test
    public void modifyUserLifeUpgradeActions_NoActionsNull() {
        LifeUpgradeAction lifeUpgradeAction = new LifeUpgradeAction();
        lifeUpgradeAction.setId(UUID.randomUUID());
        lifeUpgradeAction.setLifeUpgradeCategoryId(UUID.randomUUID());
        lifeUpgradeAction.setName("Test");

        repo.modifyUserLifeUpgradeActions(UUID.randomUUID(), null);
    }

    @Test
    public void modifyUserLifeUpgradeActions_CreateAnItem() {
        final UUID categoryA = sampleData.lifeUpgradeCategory();
        final UUID categoryB = sampleData.lifeUpgradeCategory();
        final UUID user = sampleData.user();
        final UUID lifeUpgradeActionId = sampleData.lifeUpgradeAction(user, categoryA);
        final String newName = "New Name ....";
        final int newTimesPerWeek = 103;

        final LifeUpgradeAction lifeUpgradeAction = new LifeUpgradeAction();
        lifeUpgradeAction.setId(lifeUpgradeActionId);
        lifeUpgradeAction.setName(newName);
        lifeUpgradeAction.setTimesPerWeek(newTimesPerWeek);
        lifeUpgradeAction.setLifeUpgradeCategoryId(categoryB);
        lifeUpgradeAction.setUserId(user);

        repo.modifyUserLifeUpgradeActions(user, Arrays.asList(lifeUpgradeAction));
        final LifeUpgradeAction newLifeUpgradeAction = repo.modifiedUserLifeUpgradeActions(user, null).get(0);

        assertEquals(newName, newLifeUpgradeAction.getName());
        assertEquals(newTimesPerWeek, newLifeUpgradeAction.getTimesPerWeek());
        assertEquals(categoryB, newLifeUpgradeAction.getLifeUpgradeCategoryId());
    }

    @Test
    public void deleteUserLifeUpgradeActions() {
        repo.deleteUserLifeUpgradeActions(UUID.randomUUID(), Arrays.asList(UUID.randomUUID(), UUID.randomUUID()));
    }

    @Test
    public void deleteUserLifeUpgradeActions_NoActions() {
        repo.deleteUserLifeUpgradeActions(UUID.randomUUID(), Collections.<UUID>emptyList());
    }

    @Test
    public void deleteUserLifeUpgradeActions_NoActionsNull() {
        repo.deleteUserLifeUpgradeActions(UUID.randomUUID(), null);
    }


    @Test
    public void modifyLifeUpgradeCategory() {
        LifeUpgradeCategory category = new LifeUpgradeCategory();
        category.setId(UUID.randomUUID());
        category.setName("Some Test Name");
        category.setIcon(new byte[] { 0, 0, 0, 0 });
        repo.modifyLifeUpgradeCategory(category);
    }

    @Test
    public void modifyLifeUpgradeCategory_NoIcon() {
        LifeUpgradeCategory category = new LifeUpgradeCategory();
        category.setId(UUID.randomUUID());
        category.setName("Some Test Name when NoIcon");
        repo.modifyLifeUpgradeCategory(category);
    }

    @Test
    public void modifyLifeUpgradeCategory_WithNull() {
        repo.modifyLifeUpgradeCategory(null);
    }

    @Test
    public void deleteLifeUpgradeCategory_nonexisting() {
        repo.deleteLifeUpgradeCategory(UUID.randomUUID());
    }

    @Test
    public void deleteLifeUpgradeCategory_existing() {
        final UUID category = sampleData.lifeUpgradeCategory();

        repo.deleteLifeUpgradeCategory(category);

        //HMITKOV: Add 3 hours to handle time difference.
        assertTrue(repo.deletedLifeUpgradeCategories(DateTime.now().minusMinutes(181)).contains(category));
    }

    @Test
    public void deleteLifeUpgradeCategory_null() {
        repo.deleteLifeUpgradeCategory(null);
    }

}