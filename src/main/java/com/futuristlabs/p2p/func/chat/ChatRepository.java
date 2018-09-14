package com.futuristlabs.p2p.func.chat;

import org.joda.time.DateTime;

import java.util.List;
import java.util.UUID;

public interface ChatRepository {

    List<ChatMessage> findAllByFriendshipAndUpdatedAfter(UUID userId, UUID friendshipId, DateTime modifiedSince);
    List<String> findAllDeviceTokensForUserFriend(UUID fromUserId, UUID friendshipId);

    void save(IncomingChatMessage message, UUID fromUserId, UUID forFriendshipId);
}

