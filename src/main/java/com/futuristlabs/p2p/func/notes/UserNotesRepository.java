package com.futuristlabs.p2p.func.notes;

import org.joda.time.DateTime;

import java.util.List;
import java.util.UUID;

public interface UserNotesRepository {
    List<UserNotes> modifiedNotes(UUID userId, DateTime modifiedSince);

    List<UUID> deletedNotes(UUID userId, DateTime modifiedSince);

    void modifyNotes(UUID userId, List<UserNotes> userNotes);

    void deleteNotes(UUID userId, List<UUID> userNotes);
}
