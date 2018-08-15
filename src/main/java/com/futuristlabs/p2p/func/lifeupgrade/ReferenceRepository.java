package com.futuristlabs.p2p.func.lifeupgrade;

import org.joda.time.DateTime;

import java.util.List;
import java.util.UUID;

public interface ReferenceRepository {

    List<LifeUpgradeCategory> modifiedLifeUpgradeCategories(DateTime modifiedSince);

    List<UUID> deletedLifeUpgradeCategories(DateTime modifiedSince);

    void modifyLifeUpgradeCategory(LifeUpgradeCategory category);

    void deleteLifeUpgradeCategory(UUID categoryId);

    List<LifeUpgradeAction> modifiedLifeUpgradeActionsForCategory(UUID categoryId);

    List<LifeUpgradeAction> modifiedLifeUpgradeActions(DateTime modifiedSince);

    List<UUID> deletedLifeUpgradeActions(DateTime modifiedSince);

    void modifyLifeUpgradeAction(LifeUpgradeAction action);

    void deleteLifeUpgradeAction(UUID actionId);

    List<LifeUpgradeAction> modifiedUserLifeUpgradeActions(UUID userId, DateTime modifiedSince);

    List<UUID> deletedUserLifeUpgradeActions(UUID userId, DateTime modifiedSince);

    void modifyUserLifeUpgradeActions(UUID userId, List<LifeUpgradeAction> lifeUpgradeActions);

    void deleteUserLifeUpgradeActions(UUID userId, List<UUID> lifeUpgradeActions);
}
