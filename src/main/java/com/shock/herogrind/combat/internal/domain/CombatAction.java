package com.shock.herogrind.combat.internal.domain;

import com.shock.herogrind.combat.api.CombatActionInfo;

import java.util.UUID;

public record CombatAction(
        CombatActionType type,
        UUID sourceId,
        UUID targetId,
        Double value,
        Double targetHealth
) {

    CombatActionInfo toInfo() {
        return CombatActionInfo.from(this);
    }

}
