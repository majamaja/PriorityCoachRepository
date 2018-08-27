package com.futuristlabs.p2p.func.lifeupgrade;

import org.joda.time.DateTime;

import java.util.List;
import java.util.UUID;

public interface ReferenceRepository {

    List<LifeUpgradeCategory> modifiedLifeUpgradeCategories(DateTime modifiedSince);

    List<UUID> deletedLifeUpgradeCategories(DateTime modifiedSince);

    void modifyLifeUpgradeCategory(LifeUpgradeCategory category);

    void deleteLifeUpgradeCategory(UUID categoryId);

    List<LifeUpgradeAction> modifiedUserLifeUpgradeActions(UUID userId, DateTime modifiedSince);

    /**
       Filter the list by permissions, granted from user to friend
     */
    List<LifeUpgradeAction> modifiedUserLifeUpgradeActionsRestricted(UUID userId, DateTime modifiedSince, UUID friendId);

    List<UUID> deletedUserLifeUpgradeActions(UUID userId, DateTime modifiedSince);

    void modifyUserLifeUpgradeActions(UUID userId, List<LifeUpgradeAction> lifeUpgradeActions);

    void deleteUserLifeUpgradeActions(UUID userId, List<UUID> lifeUpgradeActions);
}
