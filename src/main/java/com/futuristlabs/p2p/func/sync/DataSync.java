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
        referenceSyncData.deleted.lifeUpgradeCategories = referenceRepository.deletedLifeUpgradeCategories(modifiedSince);

        return referenceSyncData;
    }

    public UserSyncData getUserData(DateTime modifiedSince, UUID userId) {
        final UserSyncData userSyncData = new UserSyncData();

        userSyncData.updated.lifeUpgradeActions = referenceRepository.modifiedUserLifeUpgradeActions(userId, modifiedSince);
        userSyncData.updated.userActionsLogs = userActionsRepository.modifiedActionsLogs(userId, modifiedSince);
        userSyncData.updated.userHappinessLevels = userHappinessRepository.modifiedHappinessLevel(userId, modifiedSince);

        userSyncData.updated.userFriends = userFriendsRepository.modifiedFriends(userId, null);
        for (UserFriend friend : userSyncData.updated.userFriends) {
//            show add data no matter the permissions
            friend.setLifeUpgradeActions(referenceRepository.modifiedUserLifeUpgradeActions(friend.getFriendId(), null));
            friend.setActionsLog(userActionsRepository.modifiedActionsLogs(friend.getFriendId(), null));
            friend.setHappinessLevels(userHappinessRepository.modifiedHappinessLevel(friend.getFriendId(), null));
        }
        userSyncData.updated.userFriendPermissions = userFriendsRepository.modifiedFriendsPermissions(userId, modifiedSince);
        userSyncData.updated.userNotes = userNotesRepository.modifiedNotes(userId, modifiedSince);

        userSyncData.deleted.lifeUpgradeActions = referenceRepository.deletedUserLifeUpgradeActions(userId, modifiedSince);
        userSyncData.deleted.userActionsLogs = userActionsRepository.deletedActionsLogs(userId, modifiedSince);
        userSyncData.deleted.userFriends = userFriendsRepository.deletedFriends(userId, modifiedSince);
        userSyncData.deleted.userFriendPermissions = userFriendsRepository.deletedFriendsPermissions(userId, modifiedSince);
        userSyncData.deleted.userNotes = userNotesRepository.deletedNotes(userId, modifiedSince);

        return userSyncData;
    }

    public void updateUserData(UserSyncData userSyncData, UUID userId) {
        referenceRepository.modifyUserLifeUpgradeActions(userId, userSyncData.updated.lifeUpgradeActions);
        userActionsRepository.modifyActionsLogs(userId, userSyncData.updated.userActionsLogs);
        userHappinessRepository.modifyHappinessLevel(userId, userSyncData.updated.userHappinessLevels);
        userFriendsRepository.modifyFriends(userId, userSyncData.updated.userFriends);
        userFriendsRepository.modifyFriendsPermissions(userId, userSyncData.updated.userFriendPermissions);
        userNotesRepository.modifyNotes(userId, userSyncData.updated.userNotes);

        System.out.println(userSyncData);
        System.out.println(userSyncData.deleted);
        System.out.println(userSyncData.deleted.lifeUpgradeActions);
        referenceRepository.deleteUserLifeUpgradeActions(userId, userSyncData.deleted.lifeUpgradeActions);
        userActionsRepository.deleteActionsLogs(userId, userSyncData.deleted.userActionsLogs);
        userFriendsRepository.deleteFriends(userId, userSyncData.deleted.userFriends);
        userFriendsRepository.deleteFriendsPermissions(userId, userSyncData.deleted.userFriendPermissions);
        userNotesRepository.deleteNotes(userId, userSyncData.deleted.userNotes);
    }
}
