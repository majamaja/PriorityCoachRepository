package com.futuristlabs.p2p.func.chat;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.futuristlabs.p2p.utils.Utils;
import org.joda.time.DateTime;

import java.util.UUID;

public class ChatMessage {
    private UUID id;
    private UUID from;
    private UUID to;
    private String content;
    private DateTime sendAt;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getFrom() {
        return from;
    }

    public void setFrom(UUID from) {
        this.from = from;
    }

    public UUID getTo() {
        return to;
    }

    public void setTo(UUID to) {
        this.to = to;
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

    @JsonIgnore
    public DateTime getSendAtAsDate() {
        return sendAt;
    }

    public void setSendAtAsDate(DateTime sendAt) {
        this.sendAt = sendAt;
    }

    @Override
    public String toString() {
        return "ChatMessage{" +
                "id=" + id +
                ", from=" + from +
                ", to=" + to +
                ", content='" + content + '\'' +
                ", sendAt=" + sendAt +
                '}';
    }
}
