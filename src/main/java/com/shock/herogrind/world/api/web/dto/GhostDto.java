package com.shock.herogrind.world.api.web.dto;

import java.util.UUID;

public record GhostDto(
    UUID heroId,
    UUID currentAreaId,
    UUID travelDestinationId,
    Long startedAt,
    Long arrivalAt,
    Long resurrectionEndAt) {
}
