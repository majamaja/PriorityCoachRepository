package com.futuristlabs.p2p.func.buddy;

import java.util.UUID;

public class UserFriendPermission {

    private UUID id;
    private UUID friendshipId;
    private UUID lifeUpgradeActionsId;
    private boolean visible;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getFriendshipId() {
        return friendshipId;
    }

    public void setFriendshipId(UUID friendshipId) {
        this.friendshipId = friendshipId;
    }

    public UUID getLifeUpgradeActionsId() {
        return lifeUpgradeActionsId;
    }

    public void setLifeUpgradeActionsId(UUID lifeUpgradeActionsId) {
        this.lifeUpgradeActionsId = lifeUpgradeActionsId;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

}
