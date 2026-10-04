package com.shock.herogrind.combat.api;

import java.util.List;
import java.util.UUID;

import com.shock.herogrind.world.internal.domain.HeroActivityState;

public record GhostInfo(
    List<UUID> pathToTown,
    UUID currentAreaId,
    UUID travelDestinationId,
    Long arrivalAt,
    Long resurrectionEndAt,
    HeroActivityState state,
    UUID heroId) {
}
