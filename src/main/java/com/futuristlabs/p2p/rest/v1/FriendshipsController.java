package com.futuristlabs.p2p.rest.v1;

import com.futuristlabs.p2p.func.auth.SessionUser;
import com.futuristlabs.p2p.func.chat.Chat;
import com.futuristlabs.p2p.func.chat.ChatMessage;
import com.futuristlabs.p2p.func.chat.IncomingChatMessage;
import com.futuristlabs.p2p.utils.Utils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@Controller
@RequestMapping("/v1/friendships")
public class FriendshipsController {

    private Chat chat;

    @Autowired
    public FriendshipsController(Chat chat) {
        this.chat = chat;
    }

    @RequestMapping(value = "/{friendshipId}/messages", method = GET)
    @ResponseBody
    public List<ChatMessage> listMessages(
            @PathVariable("friendshipId") UUID friendshipId,
            @RequestHeader(value = "If-Modified-Since", required = false) String modifiedSinceStr,
            @AuthenticationPrincipal SessionUser user
                                         ) {
        final DateTime modifiedSince = Utils.parseDate(modifiedSinceStr);
        return chat.newMessages(user.getId(), friendshipId, modifiedSince);
    }

    @RequestMapping(value = "/{friendshipId}/messages", method = POST)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void sendMessage(
            @PathVariable("friendshipId") UUID friendshipId,
            @AuthenticationPrincipal SessionUser user,
            @RequestBody IncomingChatMessage message
                           ) {
        chat.postMessage(message, user.getId(), friendshipId);
    }

}

