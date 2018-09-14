package com.futuristlabs.p2p.repos.jdbc;

import com.futuristlabs.p2p.func.chat.ChatMessage;
import com.futuristlabs.p2p.func.chat.ChatRepository;
import com.futuristlabs.p2p.func.chat.IncomingChatMessage;
import com.futuristlabs.p2p.repos.RepositoryTest;
import org.joda.time.DateTime;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ChatRepositoryTest extends RepositoryTest {

    @Autowired
    private ChatRepository repo;

    @Test
    public void modifiedMessages() {
        final List<ChatMessage> messages = repo.findAllByFriendshipAndUpdatedAfter(UUID.randomUUID(), UUID.randomUUID(), new DateTime());
        assertTrue(messages.isEmpty());
    }

    @Test
    public void modifiedMessagesNoSinceDate() {
        final List<ChatMessage> messages = repo.findAllByFriendshipAndUpdatedAfter(UUID.randomUUID(), UUID.randomUUID(), null);
        assertTrue(messages.isEmpty());
    }

    @Test
    public void createMessage() {
        final UUID fromUserId = sampleData.user();
        final UUID friendId = sampleData.user();
        final UUID friendshipId = sampleData.friendship(fromUserId, friendId);

        final IncomingChatMessage message = new IncomingChatMessage();
        message.setId(UUID.randomUUID());
        message.setContent("Sample Content");
        message.setSendAtDate(new DateTime());
        repo.save(message, fromUserId, friendshipId);
    }

    @Test
    public void modifiedMessagesAfterCreate() {
        final UUID messageId = UUID.randomUUID();
        final UUID fromUserId = sampleData.user();
        final UUID friendId = sampleData.user();
        final UUID friendshipId = sampleData.friendship(fromUserId, friendId);

        final IncomingChatMessage message = new IncomingChatMessage();
        message.setId(messageId);
        message.setContent("Sample Content");
        message.setSendAtDate(new DateTime());
        repo.save(message, fromUserId, friendshipId);

        assertEquals(1, repo.findAllByFriendshipAndUpdatedAfter(fromUserId, friendshipId, null).size());
        assertEquals(0, repo.findAllByFriendshipAndUpdatedAfter(fromUserId, friendshipId, new DateTime()).size());
        assertEquals(1, repo.findAllByFriendshipAndUpdatedAfter(fromUserId, friendshipId, new DateTime().minusHours(1)).size());
    }

    @Test
    public void userDeviceApnTokens() {
        final UUID fromUserId = UUID.randomUUID();
        final UUID friendshipId = UUID.randomUUID();
        final List<String> apnTokens = repo.findAllDeviceTokensForUserFriend(fromUserId, friendshipId);

        assertTrue(apnTokens.isEmpty());
    }
}