package com.futuristlabs.p2p.func.sync;

import com.futuristlabs.p2p.func.buddy.UserFriend;
import com.futuristlabs.p2p.func.buddy.UserFriendsRepository;
import com.futuristlabs.p2p.func.happiness.UserHappinessRepository;
import com.futuristlabs.p2p.func.lifeupgrade.ReferenceRepository;
import com.futuristlabs.p2p.func.notes.UserNotesRepository;
import com.futuristlabs.p2p.func.useractions.UserActionsRepository;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class DataSync {

    @Autowired
    private ReferenceRepository referenceRepository;

    @Autowired
    private UserActionsRepository userActionsRepository;

    @Autowired
    private UserHappinessRepository userHappinessRepository;

    @Autowired
    private UserFriendsRepository userFriendsRepository;

    @Autowired
    private UserNotesRepository userNotesRepository;

    public ReferenceSyncData getReferenceData(DateTime modifiedSince) {
        final ReferenceSyncData referenceSyncData = new ReferenceSyncData();

        referenceSyncData.updated.lifeUpgradeCategories = referenceRepository.modifiedLifeUpgradeCategories(modifiedSince);
        referenceSyncData.updated.lifeUpgradeActions = referenceRepository.modifiedLifeUpgradeActions(modifiedSince);

        referenceSyncData.deleted.lifeUpgradeCategories = referenceRepository.deletedLifeUpgradeCategories(modifiedSince);
        referenceSyncData.deleted.lifeUpgradeActions = referenceRepository.deletedLifeUpgradeActions(modifiedSince);

        return referenceSyncData;
    }

    public UserSyncData getUserData(DateTime modifiedSince, UUID userId) {
        final UserSyncData userSyncData = new UserSyncData();

        userSyncData.updated.lifeUpgradeActions = referenceRepository.modifiedUserLifeUpgradeActions(userId, modifiedSince);
        userSyncData.updated.userActionItems = userActionsRepository.modifiedActionItems(userId, modifiedSince);
        userSyncData.updated.userActionsLogs = userActionsRepository.modifiedActionsLogs(userId, modifiedSince);
        userSyncData.updated.userHappinessLevels = userHappinessRepository.modifiedHappinessLevel(userId, modifiedSince);
        userSyncData.updated.userFriends = userFriendsRepository.modifiedFriends(userId, modifiedSince);
        for (UserFriend friend : userSyncData.updated.userFriends) {
//            show add data no matter the permissions

            friend.setLifeUpgradeActions(referenceRepository.modifiedUserLifeUpgradeActions(friend.getFriendId(), null));
            friend.setFriendActions(userActionsRepository.modifiedActionItems(friend.getFriendId(), null));
            friend.setFriendActionsLog(userActionsRepository.modifiedActionsLogs(friend.getFriendId(), null));

//            friend.setLifeUpgradeActions(referenceRepository.modifiedUserLifeUpgradeActionsFilteredByPermissions(friend.getFriendId(), null, userId));
//            friend.setFriendActions(userActionsRepository.modifiedActionItemsFilteredByPermissions(friend.getFriendId(), null, userId));
//            friend.setFriendActionsLog(userActionsRepository.modifiedActionsLogsFilteredByPermissions(friend.getFriendId(), null, userId));
        }
        userSyncData.updated.userFriendPermissions = userFriendsRepository.modifiedFriendsPermissions(userId, modifiedSince);
        userSyncData.updated.userNotes = userNotesRepository.modifiedNotes(userId, modifiedSince);

        userSyncData.deleted.lifeUpgradeActions = referenceRepository.deletedUserLifeUpgradeActions(userId, modifiedSince);
        userSyncData.deleted.userActionItems = userActionsRepository.deletedActionItems(userId, modifiedSince);
        userSyncData.deleted.userActionsLogs = userActionsRepository.deletedActionsLogs(userId, modifiedSince);
        userSyncData.deleted.userFriends = userFriendsRepository.deletedFriends(userId, modifiedSince);
        userSyncData.deleted.userFriendPermissions = userFriendsRepository.deletedFriendsPermissions(userId, modifiedSince);
        userSyncData.deleted.userNotes = userNotesRepository.deletedNotes(userId, modifiedSince);

        return userSyncData;
    }

    public void updateUserData(UserSyncData userSyncData, UUID userId) {
        referenceRepository.modifyUserLifeUpgradeActions(userId, userSyncData.updated.lifeUpgradeActions);
        userActionsRepository.modifyActionItems(userId, userSyncData.updated.userActionItems);
        userActionsRepository.modifyActionsLogs(userId, userSyncData.updated.userActionsLogs);
        userHappinessRepository.modifyHappinessLevel(userId, userSyncData.updated.userHappinessLevels);
        userFriendsRepository.modifyFriends(userId, userSyncData.updated.userFriends);
        userFriendsRepository.modifyFriendsPermissions(userId, userSyncData.updated.userFriendPermissions);
        userNotesRepository.modifyNotes(userId, userSyncData.updated.userNotes);

        referenceRepository.deleteUserLifeUpgradeActions(userId, userSyncData.deleted.lifeUpgradeActions);
        userActionsRepository.deleteActionItems(userId, userSyncData.deleted.userActionItems);
        userActionsRepository.deleteActionsLogs(userId, userSyncData.deleted.userActionsLogs);
        userFriendsRepository.deleteFriends(userId, userSyncData.deleted.userFriends);
        userFriendsRepository.deleteFriendsPermissions(userId, userSyncData.deleted.userFriendPermissions);
        userNotesRepository.deleteNotes(userId, userSyncData.deleted.userNotes);
    }
}
