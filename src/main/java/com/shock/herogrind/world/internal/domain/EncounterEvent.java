package com.shock.herogrind.world.internal.domain;

import java.util.UUID;

public record EncounterEvent(
    UUID encounterId,
    UUID heroId,
    Double heroHealth,
    UUID monsterId,
    Double monsterHealth) implements WorldEventPayload {
}
