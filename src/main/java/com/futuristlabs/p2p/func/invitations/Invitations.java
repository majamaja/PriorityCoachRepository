package com.futuristlabs.p2p.func.invitations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class Invitations {

    private InvitationsRepository invitationsRepository;

    @Autowired
    public Invitations(InvitationsRepository invitationsRepository) {
        this.invitationsRepository = invitationsRepository;
    }

    public String create(UUID userId, BuddyInvitation invitation) {
        return invitationsRepository.create(userId, invitation.getEmail(), invitation.getPhone());
    }

    public InvitationDetails findByToken(String invitationToken) {
        return invitationsRepository.findDetailsByInvitationToken(invitationToken);
    }

    public boolean accept(UUID userId, String invitationToken) {
        if (invitationsRepository.deleteIfExists(userId, invitationToken)) {
            invitationsRepository.markAsFriend(userId, invitationToken);
            return true;
        } else {
            return false;
        }
    }

    public boolean reject(UUID userId, String invitationToken) {
        return invitationsRepository.deleteIfExists(userId, invitationToken);
    }
}
