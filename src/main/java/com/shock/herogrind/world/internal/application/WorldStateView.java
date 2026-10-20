package com.shock.herogrind.world.internal.application;

import java.util.List;
import java.util.UUID;

import com.shock.herogrind.world.internal.domain.ActorType;
import com.shock.herogrind.world.internal.domain.WorldActorBehavior;

public record WorldStateView(
    List<HeroActivityView> heroActivities,
    List<EncounterView> encounters,
    List<GhostView> ghosts,
    List<ActorView> actors) {

  public record EncounterView(
      UUID encounterId,
      UUID heroId,
      Double heroHealth,
      UUID enemyId,
      Double enemyHealth) {
  }

  public record GhostView(
      UUID heroId,
      UUID currentAreaId,
      UUID travelDestinationId,
      Long startedAt,
      Long arrivalAt,
      Long resurrectionEndAt) {
  }

  public record ActorView(
      UUID id,
      ActorType type,
      UUID areaId,
      double positionX,
      double positionY,
      WorldActorBehavior behavior,
      UUID targetId,
      double health,
      double maxHealth) {
  }
}
