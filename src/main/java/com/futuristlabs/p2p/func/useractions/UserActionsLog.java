package com.futuristlabs.p2p.func.useractions;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.futuristlabs.p2p.utils.Utils;
import org.joda.time.DateTime;

import java.util.UUID;

public class UserActionsLog {

    private UUID id;
    private UUID userActionItemId;
    private DateTime actionDate;
    private int timesDone;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getUserActionItemId() {
        return userActionItemId;
    }

    public void setUserActionItemId(UUID userActionItemId) {
        this.userActionItemId = userActionItemId;
    }

    public String getActionDate() {
        return Utils.toString(actionDate);
    }

    public void setActionDate(String actionDate) {
        this.actionDate = Utils.parseDate(actionDate);
    }

    @JsonIgnore
    public DateTime getActionDateAsDate() {
        return actionDate;
    }

    public void setActionDateAsDate(DateTime actionDate) {
        this.actionDate = actionDate;
    }

    public int getTimesDone() {
        return timesDone;
    }

    public void setTimesDone(int timesDone) {
        this.timesDone = timesDone;
    }
}
