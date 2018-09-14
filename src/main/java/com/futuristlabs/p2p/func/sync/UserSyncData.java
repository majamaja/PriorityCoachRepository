package com.futuristlabs.p2p.func.sync;

import com.futuristlabs.p2p.func.buddy.UserFriend;
import com.futuristlabs.p2p.func.buddy.UserFriendPermission;
import com.futuristlabs.p2p.func.happiness.HappinessLevel;
import com.futuristlabs.p2p.func.lifeupgrade.LifeUpgradeAction;
import com.futuristlabs.p2p.func.useractions.UserActionsLog;
import org.codehaus.jackson.annotate.JsonIgnore;

import java.util.List;
import java.util.UUID;

public class UserSyncData {

    public UpdatedUserSyncData updated = new UpdatedUserSyncData();
    public DeletedUserSyncData deleted = new DeletedUserSyncData();
    public FailedUserSyncData failed = new FailedUserSyncData();

    @JsonIgnore
    public boolean isEmpty() {
        return updated.isEmpty() && deleted.isEmpty() && failed.isEmpty();
    }

    public static abstract class AbstractUserSyncData<LIFE_UPGRADE_ACTIONS, USER_ACTIONS_LOG, USER_HAPPINESS_LEVEL, USER_FRIENDS, USER_FRIEND_PERMISSION> {
        public List<LIFE_UPGRADE_ACTIONS> lifeUpgradeActions;
        public List<USER_ACTIONS_LOG> userActionsLogs;
        public List<USER_HAPPINESS_LEVEL> userHappinessLevels;
        public List<USER_FRIENDS> userFriends;
        public List<USER_FRIEND_PERMISSION> userFriendPermissions;

        public boolean isEmpty() {
            return (lifeUpgradeActions == null || lifeUpgradeActions.isEmpty()) &&
                   (userActionsLogs == null || userActionsLogs.isEmpty()) &&
                   (userHappinessLevels == null || userHappinessLevels.isEmpty()) &&
                   (userFriends == null || userFriends.isEmpty()) &&
                   (userFriendPermissions == null || userFriendPermissions.isEmpty());
        }

    }

    public static class UpdatedUserSyncData extends AbstractUserSyncData<LifeUpgradeAction, UserActionsLog, HappinessLevel, UserFriend, UserFriendPermission> {

    }

    public static class DeletedUserSyncData extends AbstractUserSyncData<UUID, UUID, UUID, UUID, UUID> {

    }

    public static class FailedUserSyncData extends AbstractUserSyncData<UUID, UUID, UUID, UUID, UUID> {

    }

}
