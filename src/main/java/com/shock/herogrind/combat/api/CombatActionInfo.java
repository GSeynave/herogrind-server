package com.shock.herogrind.combat.api;

import com.shock.herogrind.combat.internal.domain.CombatAction;

import java.util.UUID;

public record CombatActionInfo(
        CombatActionTypeInfo type,
        UUID sourceId,
        UUID targetId,
        double targetHealth,
        double value
) {

    public static CombatActionInfo from(CombatAction action) {
        return new CombatActionInfo(
                CombatActionTypeInfo.valueOf(action.type().name()),
                action.sourceId(),
                action.targetId(),
                action.targetHealth(),
                action.value()
        );
    }

}
