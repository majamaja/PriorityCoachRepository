package com.futuristlabs.p2p.func.chat;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
public class Chat {

    private ChatRepository chatRepository;
    private NotificationProvider notificationProvider;

    @Autowired
    public Chat(ChatRepository chatRepository, NotificationProvider notificationProvider) {
        this.chatRepository = chatRepository;
        this.notificationProvider = notificationProvider;
    }

    public List<ChatMessage> newMessages(UUID friendshipId, DateTime modifiedSince) {
        return chatRepository.findAllByFriendshipAndUpdatedAfter(friendshipId, modifiedSince);
    }

    public void postMessage(IncomingChatMessage message, UUID fromUserId, UUID forFriendshipId) {
        if (null == message.getId()) {
            message.setId(UUID.randomUUID());
        }
        chatRepository.save(message, fromUserId, forFriendshipId);

        final List<String> tokensToNotify = chatRepository.findAllDeviceTokensForUserFriend(fromUserId, forFriendshipId);
        notificationProvider.notifyAll(tokensToNotify, message.getContent());
    }
}
