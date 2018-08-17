package com.futuristlabs.p2p.repos.jdbc;

import com.futuristlabs.p2p.func.lifeupgrade.LifeUpgradeAction;
import com.futuristlabs.p2p.func.lifeupgrade.LifeUpgradeCategory;
import com.futuristlabs.p2p.func.lifeupgrade.ReferenceRepository;
import com.futuristlabs.p2p.repos.RepositoryTest;
import org.joda.time.DateTime;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

import static org.junit.Assert.assertFalse;
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
    public void deletedLifeUpgradeCategoriesNoDate() {
        final List<UUID> uuids = repo.deletedLifeUpgradeCategories(null);
        assertTrue(uuids.isEmpty());
    }

    @Test
    public void modifiedLifeUpgradeActions() {
        final List<LifeUpgradeAction> actions = repo.modifiedLifeUpgradeActions(new DateTime());
        assertTrue(actions.isEmpty());
    }

    @Test
    public void modifiedLifeUpgradeActionsNoDate() {
        final List<LifeUpgradeAction> actions = repo.modifiedLifeUpgradeActions(null);
        assertTrue(actions.isEmpty());
    }

    @Test
    public void deletedLifeUpgradeActions() {
        final List<UUID> uuids = repo.deletedLifeUpgradeActions(new DateTime());
        assertTrue(uuids.isEmpty());
    }

    @Test
    public void deletedLifeUpgradeActionsNoDate() {
        final List<UUID> uuids = repo.deletedLifeUpgradeActions(null);
        assertTrue(uuids.isEmpty());
    }

    @Test
    public void modifiedUserLifeUpgradeActions() {
        final List<LifeUpgradeAction> actions = repo.modifiedUserLifeUpgradeActions(UUID.randomUUID(), new DateTime());
        assertTrue(actions.isEmpty());
    }

    @Test
    public void modifiedUserLifeUpgradeActionsNoDate() {
        final List<LifeUpgradeAction> actions = repo.modifiedUserLifeUpgradeActions(UUID.randomUUID(), null);
        assertTrue(actions.isEmpty());
    }

    @Test
    public void deletedUserLifeUpgradeActions() {
        final List<UUID> uuids = repo.deletedUserLifeUpgradeActions(UUID.randomUUID(), new DateTime());
        assertTrue(uuids.isEmpty());
    }

    @Test
    public void deletedUserLifeUpgradeActionsNoDate() {
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
    public void modifyUserLifeUpgradeActionsNoActions() {
        LifeUpgradeAction lifeUpgradeAction = new LifeUpgradeAction();
        lifeUpgradeAction.setId(UUID.randomUUID());
        lifeUpgradeAction.setLifeUpgradeCategoryId(UUID.randomUUID());
        lifeUpgradeAction.setName("Test");

        repo.modifyUserLifeUpgradeActions(UUID.randomUUID(), new ArrayList<LifeUpgradeAction>());
    }

    @Test
    public void modifyUserLifeUpgradeActionsNoActionsNull() {
        LifeUpgradeAction lifeUpgradeAction = new LifeUpgradeAction();
        lifeUpgradeAction.setId(UUID.randomUUID());
        lifeUpgradeAction.setLifeUpgradeCategoryId(UUID.randomUUID());
        lifeUpgradeAction.setName("Test");

        repo.modifyUserLifeUpgradeActions(UUID.randomUUID(), null);
    }

    @Test
    public void deleteUserLifeUpgradeActions() {
        repo.deleteUserLifeUpgradeActions(UUID.randomUUID(), Arrays.asList(UUID.randomUUID(), UUID.randomUUID()));
    }

    @Test
    public void deleteUserLifeUpgradeActionsNoActions() {
        repo.deleteUserLifeUpgradeActions(UUID.randomUUID(), Collections.<UUID>emptyList());
    }

    @Test
    public void deleteUserLifeUpgradeActionsNoActionsNull() {
        repo.deleteUserLifeUpgradeActions(UUID.randomUUID(), null);
    }


    @Test
    public void modifyLifeUpgradeCategory() {
        LifeUpgradeCategory category = new LifeUpgradeCategory();
        category.setId(UUID.randomUUID());
        category.setName("Some Test Name");
        category.setIcon(new byte[]{0, 0, 0, 0});
        repo.modifyLifeUpgradeCategory(category);
    }

    @Test
    public void modifyLifeUpgradeCategoryNoIcon() {
        LifeUpgradeCategory category = new LifeUpgradeCategory();
        category.setId(UUID.randomUUID());
        category.setName("Some Test Name when NoIcon");
        repo.modifyLifeUpgradeCategory(category);
    }

    @Test
    public void modifyLifeUpgradeCategoryWithNull() {
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

        assertTrue(repo.deletedLifeUpgradeCategories(DateTime.now().minusMinutes(1)).contains(category));
    }

    @Test
    public void deleteLifeUpgradeCategory_null() {
        repo.deleteLifeUpgradeCategory(null);
    }


    @Test
    public void modifiedLifeUpgradeActionsForCategory() {
        assertTrue(repo.modifiedLifeUpgradeActionsForCategory(UUID.randomUUID()).isEmpty());
    }

    @Test
    public void modifiedLifeUpgradeActionsForCategoryWithNull() {
        assertTrue(repo.modifiedLifeUpgradeActionsForCategory(null).isEmpty());
    }

    @Test
    public void modifyLifeUpgradeAction() {
        final UUID lifeUpgradeCategory = sampleData.lifeUpgradeCategory();

        final LifeUpgradeAction action = new LifeUpgradeAction();
        action.setId(UUID.randomUUID());
        action.setLifeUpgradeCategoryId(lifeUpgradeCategory);
        action.setName("Test name");

        repo.modifyLifeUpgradeAction(action);
    }

    @Test
    public void modifyLifeUpgradeActionWithNull() {
        repo.modifyLifeUpgradeAction(null);
    }

    @Test
    public void deleteLifeUpgradeAction() {
        repo.deleteLifeUpgradeAction(UUID.randomUUID());
    }

    @Test
    public void deleteLifeUpgradeActionNull() {
        repo.deleteLifeUpgradeAction(null);
    }

}