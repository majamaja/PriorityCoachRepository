package com.futuristlabs.p2p.func.happiness;

import org.joda.time.DateTime;

import java.util.List;
import java.util.UUID;

public interface UserHappinessRepository {
    List<HappinessLevel> modifiedHappinessLevel(UUID userId, DateTime modifiedSince);

    void modifyHappinessLevel(UUID userId, List<HappinessLevel> userHappinessLevels);
}
