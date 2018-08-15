package com.futuristlabs.p2p.func.invitations;

import java.util.UUID;

public interface InvitationsRepository {

    String create(UUID userId, String email, String phone);

    boolean deleteIfExists(UUID userId, String invitationToken);

    void markAsFriend(UUID userId, String invitationToken);

    InvitationDetails findDetailsByInvitationToken(String invitationToken);
}

