package com.futuristlabs.p2p.func.useractions;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.futuristlabs.p2p.utils.Utils;
import org.joda.time.DateTime;

import java.util.UUID;

public class UserActionItem {

    private UUID id;
    private UUID userId;
    private UUID actionId;
    private DateTime startDate;
    private UserActionItemFrequency frequency;
    private int timesPerDay;
    private int everyXDay;
    private boolean dayOfWeekMon;
    private boolean dayOfWeekTue;
    private boolean dayOfWeekWed;
    private boolean dayOfWeekThr;
    private boolean dayOfWeekFri;
    private boolean dayOfWeekSat;
    private boolean dayOfWeekSun;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public UUID getActionId() {
        return actionId;
    }

    public void setActionId(UUID actionId) {
        this.actionId = actionId;
    }

    public String getStartDate() {
        return Utils.toString(startDate);
    }

    public void setStartDate(String startDate) {
        this.startDate = Utils.parseDate(startDate);
    }

    @JsonIgnore
    public DateTime getStartDateAsDate() {
        return startDate;
    }

    public void setStartDateAsDate(DateTime startDate) {
        this.startDate = startDate;
    }

    public UserActionItemFrequency getFrequency() {
        return frequency;
    }

    public void setFrequency(UserActionItemFrequency frequency) {
        this.frequency = frequency;
    }

    public int getTimesPerDay() {
        return timesPerDay;
    }

    public void setTimesPerDay(int timesPerDay) {
        this.timesPerDay = timesPerDay;
    }

    public int getEveryXDay() {
        return everyXDay;
    }

    public void setEveryXDay(int everyXDay) {
        this.everyXDay = everyXDay;
    }

    public boolean isDayOfWeekMon() {
        return dayOfWeekMon;
    }

    public void setDayOfWeekMon(boolean dayOfWeekMon) {
        this.dayOfWeekMon = dayOfWeekMon;
    }

    public boolean isDayOfWeekTue() {
        return dayOfWeekTue;
    }

    public void setDayOfWeekTue(boolean dayOfWeekTue) {
        this.dayOfWeekTue = dayOfWeekTue;
    }

    public boolean isDayOfWeekWed() {
        return dayOfWeekWed;
    }

    public void setDayOfWeekWed(boolean dayOfWeekWed) {
        this.dayOfWeekWed = dayOfWeekWed;
    }

    public boolean isDayOfWeekThr() {
        return dayOfWeekThr;
    }

    public void setDayOfWeekThr(boolean dayOfWeekThr) {
        this.dayOfWeekThr = dayOfWeekThr;
    }

    public boolean isDayOfWeekFri() {
        return dayOfWeekFri;
    }

    public void setDayOfWeekFri(boolean dayOfWeekFri) {
        this.dayOfWeekFri = dayOfWeekFri;
    }

    public boolean isDayOfWeekSat() {
        return dayOfWeekSat;
    }

    public void setDayOfWeekSat(boolean dayOfWeekSat) {
        this.dayOfWeekSat = dayOfWeekSat;
    }

    public boolean isDayOfWeekSun() {
        return dayOfWeekSun;
    }

    public void setDayOfWeekSun(boolean dayOfWeekSun) {
        this.dayOfWeekSun = dayOfWeekSun;
    }
}
