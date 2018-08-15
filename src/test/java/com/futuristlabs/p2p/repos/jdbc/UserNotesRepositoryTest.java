package com.futuristlabs.p2p.repos.jdbc;

import com.futuristlabs.p2p.func.notes.UserNotes;
import com.futuristlabs.p2p.func.notes.UserNotesRepository;
import com.futuristlabs.p2p.repos.RepositoryTest;
import org.joda.time.DateTime;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.assertTrue;

public class UserNotesRepositoryTest extends RepositoryTest {

    @Autowired
    private UserNotesRepository repo;

    @Test
    public void modifiedNotes() {
        final List<UserNotes> categories = repo.modifiedNotes(UUID.randomUUID(), new DateTime());
        assertTrue(categories.isEmpty());
    }

    @Test
    public void modifiedNotesNoDate() {
        final List<UserNotes> categories = repo.modifiedNotes(UUID.randomUUID(), null);
        assertTrue(categories.isEmpty());
    }

    @Test
    public void deletedNotes() {
        final List<UUID> uuids = repo.deletedNotes(UUID.randomUUID(), new DateTime());
        assertTrue(uuids.isEmpty());
    }

    @Test
    public void deletedNotesNoDate() {
        final List<UUID> uuids = repo.deletedNotes(UUID.randomUUID(), null);
        assertTrue(uuids.isEmpty());
    }

    @Test
    public void modifyNotes() {
        final UUID userId = sampleData.user();

        final UserNotes userNotes = new UserNotes();
        userNotes.setId(UUID.randomUUID());
        userNotes.setUserId(userId);
        userNotes.setTitle("Note TEST Title");
        userNotes.setContent("Note TEST TEST TEST content");
        userNotes.setDateAsDate(new DateTime());

        repo.modifyNotes(userId, Arrays.asList(userNotes));

        userNotes.setTitle(userNotes.getTitle() + " ...");
        repo.modifyNotes(userId, Arrays.asList(userNotes));
    }

    @Test
    public void modifyNotesNoData() {
        repo.modifyNotes(UUID.randomUUID(), new ArrayList<UserNotes>());
    }

    @Test
    public void modifyNotesNoDataNull() {
        repo.modifyNotes(UUID.randomUUID(), null);
    }

    @Test
    public void deleteNotes() {
        repo.deleteNotes(UUID.randomUUID(), Arrays.asList(UUID.randomUUID()));
    }

    @Test
    public void deleteNotesNoData() {
        repo.deleteNotes(UUID.randomUUID(), new ArrayList<UUID>());
    }

    @Test
    public void deleteNotesNoDataNull() {
        repo.deleteNotes(UUID.randomUUID(), null);
    }
}