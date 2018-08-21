package com.futuristlabs.p2p.func.happiness;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.futuristlabs.p2p.utils.Utils;
import org.joda.time.LocalDate;

import java.util.UUID;

public class HappinessLevel {
    private UUID id;
    private int level;
    private LocalDate checkinDate;
    private UUID userId;

    public HappinessLevel() {
    }

    public HappinessLevel(UUID id, int level, LocalDate checkinDate, UUID userId) {
        this.id = id;
        this.level = level;
        this.checkinDate = checkinDate;
        this.userId = userId;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getCheckinDate() {
        return Utils.toString(checkinDate);
    }

    public void setCheckinDate(String checkinDate) {
        this.checkinDate = Utils.parseDate(checkinDate).toLocalDate();
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(final UUID userId) {
        this.userId = userId;
    }

    @JsonIgnore
    public LocalDate getCheckinDateAsDate() {
        return checkinDate;
    }

    public void setCheckinDateAsDate(LocalDate checkinDate) {
        this.checkinDate = checkinDate;
    }
}
