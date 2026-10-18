package com.shock.herogrind.world.internal.application;

import java.util.List;
import java.util.UUID;

public record WorldStateView(
    List<HeroActivityView> heroActivities,
    List<EncounterView> encounters,
    List<GhostView> ghosts) {

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
}
