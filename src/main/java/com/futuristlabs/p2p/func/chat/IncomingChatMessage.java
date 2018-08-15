package com.futuristlabs.p2p.func.chat;

import com.futuristlabs.p2p.utils.Utils;
import org.joda.time.DateTime;

import java.util.UUID;

public class IncomingChatMessage {
    private UUID id;
    private String content;
    private DateTime sendAt;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSendAt() {
        return Utils.toString(sendAt);
    }

    public void setSendAt(String sendAt) {
        this.sendAt = Utils.parseDate(sendAt);
    }

    public DateTime getSendAtDate() {
        return sendAt;
    }

    public void setSendAtDate(DateTime sendAt) {
        this.sendAt = sendAt;
    }
}
