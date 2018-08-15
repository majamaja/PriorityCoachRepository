package com.futuristlabs.p2p.rest.v1;

import com.futuristlabs.p2p.func.chat.Chat;
import com.futuristlabs.p2p.func.chat.ChatMessage;
import com.futuristlabs.p2p.func.chat.IncomingChatMessage;
import com.futuristlabs.p2p.utils.Utils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
            @RequestParam("userId") UUID userId,
            @PathVariable("friendshipId") UUID friendshipId,
            @RequestHeader(value = "If-Modified-Since", required = false) String modifiedSinceStr
                                         ) {
        final DateTime modifiedSince = Utils.parseDate(modifiedSinceStr);
        return chat.newMessages(friendshipId, modifiedSince);
    }

    @RequestMapping(value = "/{friendshipId}/messages", method = POST)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void sendMessage(
            @RequestParam("userId") UUID userId,
            @PathVariable("friendshipId") UUID friendshipId,
            @RequestBody IncomingChatMessage message
    ) {
        chat.postMessage(message, userId, friendshipId);
    }

}

