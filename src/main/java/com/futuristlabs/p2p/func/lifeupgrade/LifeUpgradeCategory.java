package com.futuristlabs.p2p.func.lifeupgrade;

import java.util.UUID;

public class LifeUpgradeCategory {

    private UUID id;
    private String name;
    private byte[] icon;

    public LifeUpgradeCategory() {
    }

    public LifeUpgradeCategory(UUID id, String name, byte[] icon) {
        this.id = id;
        this.name = name;
        this.icon = icon;
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

    public byte[] getIcon() {
        return icon;
    }

    public void setIcon(byte[] icon) {
        this.icon = icon;
    }
}
