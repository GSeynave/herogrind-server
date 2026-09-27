package com.shock.herogrind.world.internal.domain;

import java.util.UUID;

public record GhostEvent(
    UUID heroId,
    UUID areaId,
    GhostState state,
    UUID currentAreaId,
    UUID travelDestinationId,
    Long arrivalAt,
    Long resurrectionEndAt) implements WorldEventPayload {
}
