package com.shock.herogrind.world.api.web.dto;

import java.util.UUID;

import com.shock.herogrind.world.internal.domain.CombatActionEvent;
import com.shock.herogrind.world.internal.domain.EncounterEvent;
import com.shock.herogrind.world.internal.domain.GhostEvent;
import com.shock.herogrind.world.internal.domain.HeroActivityEvent;
import com.shock.herogrind.world.internal.domain.WorldEvent;
import com.shock.herogrind.world.internal.domain.WorldEventPayload;

public record WorldEventDto(
    String eventType,
    WorldEventPayloadDto payload,
    Long occurredAt) {

  public static WorldEventDto from(WorldEvent event) {
    return new WorldEventDto(
        event.eventType().name(),
        WorldEventPayloadDto.from(event.payload()),
        event.occurredAt());
  }
}

sealed interface WorldEventPayloadDto
    permits HeroActivityEventDto, EncounterEventDto, CombatActionEventDto, GhostEventDto {

  static WorldEventPayloadDto from(WorldEventPayload payload) {
    return switch (payload) {
      case HeroActivityEvent event -> new HeroActivityEventDto(event.heroId(), event.areaId());
      case EncounterEvent event -> new EncounterEventDto(
          event.encounterId(), event.heroId(), event.heroHealth(), event.monsterId(), event.monsterHealth());
      case CombatActionEvent event -> new CombatActionEventDto(
          event.sourceId(), event.targetId(), event.targetHealth(), event.actionType().name(), event.value());
      case GhostEvent event -> new GhostEventDto(
          event.heroId(), event.areaId(), event.state().name(), event.currentAreaId(), event.travelDestinationId(),
          event.arrivalAt(), event.resurrectionEndAt());
    };
  }
}

record HeroActivityEventDto(UUID heroId, UUID areaId) implements WorldEventPayloadDto {
}

record EncounterEventDto(
    UUID encounterId,
    UUID heroId,
    Double heroHealth,
    UUID monsterId,
    Double monsterHealth) implements WorldEventPayloadDto {
}

record CombatActionEventDto(
    UUID sourceId,
    UUID targetId,
    double targetHealth,
    String actionType,
    double value) implements WorldEventPayloadDto {
}

record GhostEventDto(
    UUID heroId,
    UUID areaId,
    String state,
    UUID currentAreaId,
    UUID travelDestinationId,
    Long arrivalAt,
    Long resurrectionEndAt) implements WorldEventPayloadDto {
}
