package com.futuristlabs.p2p.func.lifeupgrade;

import java.util.UUID;

public class LifeUpgradeAction {

    private UUID id;
    private String name;
    private UUID lifeUpgradeCategoryId;
    private boolean isCustom;
    private UUID userId;

    public LifeUpgradeAction() {
    }

    private LifeUpgradeAction(UUID id, String name, UUID lifeUpgradeCategoryId, boolean isCustom, UUID userId) {
        this.id = id;
        this.name = name;
        this.lifeUpgradeCategoryId = lifeUpgradeCategoryId;
        this.isCustom = isCustom;
        this.userId = userId;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public UUID getLifeUpgradeCategoryId() {
        return lifeUpgradeCategoryId;
    }

    public void setLifeUpgradeCategoryId(UUID lifeUpgradeCategoryId) {
        this.lifeUpgradeCategoryId = lifeUpgradeCategoryId;
    }

    public boolean isCustom() {
        return isCustom;
    }

    public void setCustom(boolean isCustom) {
        this.isCustom = isCustom;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }
}
