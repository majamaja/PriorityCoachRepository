package com.futuristlabs.p2p.func.happiness;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.futuristlabs.p2p.utils.Utils;
import org.joda.time.LocalDate;

import java.util.UUID;

public class HappinessLevel {
    private UUID id;
    private int level;
    private LocalDate checkinDate;

    public HappinessLevel() {
    }

    public HappinessLevel(UUID id, int level, LocalDate checkinDate) {
        this.id = id;
        this.level = level;
        this.checkinDate = checkinDate;
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

    @JsonIgnore
    public LocalDate getCheckinDateAsDate() {
        return checkinDate;
    }

    public void setCheckinDateAsDate(LocalDate checkinDate) {
        this.checkinDate = checkinDate;
    }
}
