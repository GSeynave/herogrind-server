package com.shock.herogrind.world.internal.application;

import java.util.UUID;

import com.shock.herogrind.world.internal.domain.HeroActivityState;

public record HeroActivityView(
    UUID heroId,
    UUID areaId,
    HeroActivityState state,
    Long startedAt,
    ActivityPayloadView payload) {

  public sealed interface ActivityPayloadView
      permits EncounterPayloadView, GhostPayloadView {
  }

  public record EncounterPayloadView(
      UUID encounterId,
      UUID enemyId)
      implements ActivityPayloadView {
  }

  public record GhostPayloadView(
      UUID currentAreaId,
      UUID travelDestinationId,
      Long arrivalAt,
      Long resurrectionEndAt)
      implements ActivityPayloadView {
  }
}
