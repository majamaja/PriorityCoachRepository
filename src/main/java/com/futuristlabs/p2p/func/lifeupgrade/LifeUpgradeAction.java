package com.futuristlabs.p2p.func.lifeupgrade;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LifeUpgradeAction {

    private UUID id;
    private String name;
    private int timesPerWeek;
    private UUID lifeUpgradeCategoryId;
    private UUID userId;

}
