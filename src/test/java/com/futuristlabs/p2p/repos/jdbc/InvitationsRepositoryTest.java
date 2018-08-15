package com.futuristlabs.p2p.repos.jdbc;

import com.futuristlabs.p2p.func.invitations.InvitationsRepository;
import com.futuristlabs.p2p.repos.RepositoryTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

import static org.junit.Assert.*;

public class InvitationsRepositoryTest extends RepositoryTest {

    @Autowired
    private InvitationsRepository repo;

    @Test
    public void createWithEmail() {
        final UUID userId = sampleData.user();
        assertNotNull(repo.create(userId, "test@example.com", null));
    }

    @Test
    public void createWithPhone() {
        final UUID userId = sampleData.user();
        assertNotNull(repo.create(userId, null, "08123123321"));
    }

    @Test
    public void createWithEmailAndPhone() {
        final UUID userId = sampleData.user();
        assertNotNull(repo.create(userId, "test@example.com", "08123123321"));
    }

    @Test
    public void markUserAsFriend() {
        repo.markAsFriend(UUID.randomUUID(), "token");
    }

    @Test
    public void deleteIfExistsWithNoData() {
        assertFalse(repo.deleteIfExists(UUID.randomUUID(), "token"));
    }

    @Test
    public void deleteIfExistsAfterCreate() {
        final UUID userId = sampleData.user();
        final UUID friendId = sampleData.user();

        final String token = repo.create(userId, "test@example.com", null);
        assertTrue(repo.deleteIfExists(friendId, token));
    }

    @Test
    public void findDetailsByToAndInvitationTokenWhenNoData() {
        assertNull(repo.findDetailsByInvitationToken("test_token"));
    }

}
