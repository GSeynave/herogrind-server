package com.shock.herogrind.world.internal.domain;

import java.util.UUID;

public record ActorCombatAction(
    UUID sourceId,
    UUID targetId,
    double damage,
    double targetHealth) {
}
