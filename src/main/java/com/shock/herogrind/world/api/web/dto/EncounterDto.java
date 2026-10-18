package com.shock.herogrind.world.api.web.dto;

import java.util.UUID;

public record EncounterDto(
    UUID encounterId,
    UUID heroId,
    Double heroHealth,
    UUID enemyId,
    Double enemyHealth) {
}
