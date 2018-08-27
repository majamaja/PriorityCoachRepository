package com.futuristlabs.p2p.repos.jdbc;


import com.futuristlabs.p2p.func.buddy.UserFriend;
import com.futuristlabs.p2p.func.buddy.UserFriendPermission;
import com.futuristlabs.p2p.func.buddy.UserFriendsRepository;
import com.futuristlabs.p2p.repos.RepositoryTest;
import org.joda.time.DateTime;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class UserFriendsRepositoryTest extends RepositoryTest {

    @Autowired
    private UserFriendsRepository repo;

    @Test
    public void findAllFriends_nonExistingUser() {
        final List<UserFriend> friends = repo.findAllFriends(UUID.randomUUID());
        assertTrue(friends.isEmpty());
    }

    @Test
    public void findAllFriends_userWithNoFriends() {
        final UUID user = sampleData.user();
        final List<UserFriend> friends = repo.findAllFriends(user);
        assertTrue(friends.isEmpty());
    }

    @Test
    public void findAllFriends_userWithFriends() {
        final UUID user = sampleData.user();
        final UUID friend = sampleData.user();
        sampleData.friendship(user, friend);

        final List<UserFriend> friends = repo.findAllFriends(user);
        assertEquals(1, friends.size());
    }

    @Test
    public void modifiedFriendsPermissions() {
        final List<UserFriendPermission> categories = repo.modifiedFriendsPermissions(UUID.randomUUID(), new DateTime());
        assertTrue(categories.isEmpty());
    }

    @Test
    public void modifiedFriendsPermissionsNoDate() {
        repo.modifiedFriendsPermissions(UUID.randomUUID(), null);
    }

    @Test
    public void deletedFriends() {
        final List<UUID> uuids = repo.deletedFriends(UUID.randomUUID(), new DateTime());
        assertTrue(uuids.isEmpty());
    }

    @Test
    public void deletedFriendsNoDate() {
        final List<UUID> uuids = repo.deletedFriends(UUID.randomUUID(), null);
        assertTrue(uuids.isEmpty());
    }

    @Test
    public void deletedFriendsPermissions() {
        final List<UUID> uuids = repo.deletedFriendsPermissions(UUID.randomUUID(), new DateTime());
        assertTrue(uuids.isEmpty());
    }

    @Test
    public void deletedFriendsPermissionsNoDate() {
        final List<UUID> uuids = repo.deletedFriendsPermissions(UUID.randomUUID(), null);
        assertTrue(uuids.isEmpty());
    }

    @Test
    public void modifyFriends() {
        final UUID userId = sampleData.user();
        final UUID friendId = sampleData.user();

        final UserFriend friend = new UserFriend();
        friend.setId(UUID.randomUUID());
        friend.setFriendName("test");
        friend.setFriendId(friendId);
        friend.setFriendEmail("someEmail@example.com");
        friend.setFriendPhone("0888 88 88 88");

        repo.modifyFriends(userId, Arrays.asList(friend));
    }

    @Test
    public void modifyFriendsNoData() {
        repo.modifyFriends(UUID.randomUUID(), new ArrayList<UserFriend>());
    }

    @Test
    public void modifyFriendsNoDataNull() {
        repo.modifyFriends(UUID.randomUUID(), null);
    }

    @Test
    public void modifyFriendsPermissions() {
        final UUID userId = sampleData.user();
        final UUID friendId = sampleData.user();
        final UUID friendshipId = sampleData.friendship(userId, friendId);
        final UUID lifeUpgradeActionsId = sampleData.lifeUpgradeAction();

        final UserFriendPermission friendPermission = new UserFriendPermission();
        friendPermission.setId(UUID.randomUUID());
        friendPermission.setFriendshipId(friendshipId);
        friendPermission.setLifeUpgradeActionsId(lifeUpgradeActionsId);
        friendPermission.setVisible(true);

        repo.modifyFriendsPermissions(userId, Arrays.asList(friendPermission));

        friendPermission.setVisible(false);
        repo.modifyFriendsPermissions(userId, Arrays.asList(friendPermission));
    }

    @Test
    public void modifyFriendsPermissionsNoData() {
        repo.modifyFriendsPermissions(UUID.randomUUID(), new ArrayList<UserFriendPermission>());
    }

    @Test
    public void modifyFriendsPermissionsNoDataNull() {
        repo.modifyFriendsPermissions(UUID.randomUUID(), null);
    }

    @Test
    public void deleteFriends() {
        repo.deleteFriends(UUID.randomUUID(), Arrays.asList(UUID.randomUUID()));
    }

    @Test
    public void deleteFriendsNoData() {
        repo.deleteFriends(UUID.randomUUID(), new ArrayList<UUID>());
    }

    @Test
    public void deleteFriendsNoDataNull() {
        repo.deleteFriends(UUID.randomUUID(), null);
    }

    @Test
    public void deleteFriendsPermissions() {
        repo.deleteFriendsPermissions(UUID.randomUUID(), Arrays.asList(UUID.randomUUID()));
    }

    @Test
    public void deleteFriendsPermissionsNoData() {
        repo.deleteFriendsPermissions(UUID.randomUUID(), new ArrayList<UUID>());
    }

    @Test
    public void deleteFriendsPermissionsNoDataNull() {
        repo.deleteFriendsPermissions(UUID.randomUUID(), null);
    }
}
