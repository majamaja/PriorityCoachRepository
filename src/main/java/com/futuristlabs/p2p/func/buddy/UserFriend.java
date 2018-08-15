package com.futuristlabs.p2p.func.buddy;

import com.futuristlabs.p2p.func.lifeupgrade.LifeUpgradeAction;
import com.futuristlabs.p2p.func.useractions.UserActionItem;
import com.futuristlabs.p2p.func.useractions.UserActionsLog;

import java.util.List;
import java.util.UUID;

public class UserFriend {

    private UUID id;
    private UUID friendId;
    private String friendName;
    private String friendEmail;
    private String friendPhone;
    private List<LifeUpgradeAction> lifeUpgradeActions;
    private List<UserActionItem> friendActions;
    private List<UserActionsLog> friendActionsLog;


    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getFriendId() {
        return friendId;
    }

    public void setFriendId(UUID friendId) {
        this.friendId = friendId;
    }

    public String getFriendName() {
        return friendName;
    }

    public void setFriendName(String friendName) {
        this.friendName = friendName;
    }

    public String getFriendEmail() {
        return friendEmail;
    }

    public void setFriendEmail(String friendEmail) {
        this.friendEmail = friendEmail;
    }

    public String getFriendPhone() {
        return friendPhone;
    }

    public void setFriendPhone(String friendPhone) {
        this.friendPhone = friendPhone;
    }

    public List<LifeUpgradeAction> getLifeUpgradeActions() {
        return lifeUpgradeActions;
    }

    public void setLifeUpgradeActions(List<LifeUpgradeAction> lifeUpgradeActions) {
        this.lifeUpgradeActions = lifeUpgradeActions;
    }

    public List<UserActionItem> getFriendActions() {
        return friendActions;
    }

    public void setFriendActions(List<UserActionItem> friendActions) {
        this.friendActions = friendActions;
    }

    public List<UserActionsLog> getFriendActionsLog() {
        return friendActionsLog;
    }

    public void setFriendActionsLog(List<UserActionsLog> friendActionsLog) {
        this.friendActionsLog = friendActionsLog;
    }

    public UserFriendInvitationStatus getStatus() {
        if (friendId != null) {
            return UserFriendInvitationStatus.ACCEPTED;
        }

        if ((friendEmail != null && !friendEmail.isEmpty()) || (friendPhone != null && !friendPhone.isEmpty())) {
            return UserFriendInvitationStatus.INVITED;
        }

        return UserFriendInvitationStatus.INVALID;
    }
}
