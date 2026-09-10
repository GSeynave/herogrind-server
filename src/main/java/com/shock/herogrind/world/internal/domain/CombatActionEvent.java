package com.shock.herogrind.world.internal.domain;

import com.shock.herogrind.combat.api.CombatActionTypeInfo;

import java.util.UUID;

public record CombatActionEvent(
        UUID sourceId,
        UUID targetId,
        double targetHealth,
        CombatActionTypeInfo actionType,
        double value
        ) implements WorldEventPayload {
}
