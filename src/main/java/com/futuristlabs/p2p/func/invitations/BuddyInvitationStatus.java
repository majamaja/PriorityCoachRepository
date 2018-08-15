package com.futuristlabs.p2p.func.invitations;

public class BuddyInvitationStatus {
    public String invitationToken;
    public boolean isMember;

    public BuddyInvitationStatus(String invitationToken, boolean isMember) {
        this.invitationToken = invitationToken;
        this.isMember = isMember;
    }
}
