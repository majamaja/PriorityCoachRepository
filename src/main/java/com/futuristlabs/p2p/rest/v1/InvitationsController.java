package com.futuristlabs.p2p.rest.v1;

import com.futuristlabs.p2p.func.auth.SessionUser;
import com.futuristlabs.p2p.func.invitations.BuddyInvitation;
import com.futuristlabs.p2p.func.invitations.BuddyInvitationStatus;
import com.futuristlabs.p2p.func.invitations.InvitationDetails;
import com.futuristlabs.p2p.func.invitations.Invitations;
import com.futuristlabs.p2p.rest.common.RestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import static org.springframework.web.bind.annotation.RequestMethod.*;

@Controller
@RequestMapping("/v1/invitations")
public class InvitationsController {

    private Invitations invitations;

    @Autowired
    public InvitationsController(Invitations invitations) {
        this.invitations = invitations;
    }

    @RequestMapping(value = "/", method = POST)
    @ResponseBody
    public BuddyInvitationStatus inviteBuddy(
            @AuthenticationPrincipal SessionUser user,
            @RequestBody BuddyInvitation invitation
                                            ) {
        final String invitationToken = invitations.create(user.getId(), invitation);
        if (invitationToken == null) {
            throw new RestException(HttpStatus.BAD_REQUEST, "Missing email or phone");
        }
        return new BuddyInvitationStatus(invitationToken, false);
    }

    @RequestMapping(value = "/{invitationToken}", method = GET)
    @ResponseBody
    public InvitationDetails getInvitationDetails(
            @PathVariable("invitationToken") String invitationToken
                                                 ) {
        final InvitationDetails invitationDetails = invitations.findByToken(invitationToken);
        if (invitationDetails == null) {
            throw new RestException(HttpStatus.NOT_FOUND, "Invitation token not found");
        }
        return invitationDetails;
    }

    @RequestMapping(value = "/{invitationToken}", method = POST)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void acceptInvitation(
            @PathVariable("invitationToken") String invitationToken,
            @AuthenticationPrincipal SessionUser user
                                ) {
        if (!invitations.accept(user.getId(), invitationToken)) {
            throw new RestException(HttpStatus.NOT_FOUND, "Invitation token not found");
        }
    }

    @RequestMapping(value = "/{invitationToken}", method = DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void rejectInvitation(
            @PathVariable("invitationToken") String invitationToken,
            @AuthenticationPrincipal SessionUser user
                                ) {
        if (!invitations.reject(user.getId(), invitationToken)) {
            throw new RestException(HttpStatus.NOT_FOUND, "Invitation token not found");
        }
    }

}

