package com.futuristlabs.p2p.func.buddy;

import org.joda.time.DateTime;

import java.util.List;
import java.util.UUID;

public interface UserFriendsRepository {
    List<UserFriend> modifiedFriends(UUID userId, DateTime modifiedSince);

    List<UserFriendPermission> modifiedFriendsPermissions(UUID userId, DateTime modifiedSince);

    List<UUID> deletedFriends(UUID userId, DateTime modifiedSince);

    List<UUID> deletedFriendsPermissions(UUID userId, DateTime modifiedSince);

    void modifyFriends(UUID userId, List<UserFriend> userFriends);

    void modifyFriendsPermissions(UUID userId, List<UserFriendPermission> userFriendPermissions);

    void deleteFriends(UUID userId, List<UUID> userFriends);

    void deleteFriendsPermissions(UUID userId, List<UUID> userFriendPermissions);
}
