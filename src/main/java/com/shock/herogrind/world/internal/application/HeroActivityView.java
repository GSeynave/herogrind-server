package com.shock.herogrind.world.internal.application;

import java.util.UUID;

import com.shock.herogrind.world.internal.domain.EncounterActivity;
import com.shock.herogrind.world.internal.domain.GhostActivity;
import com.shock.herogrind.world.internal.domain.HeroActivity;
import com.shock.herogrind.world.internal.domain.HeroActivityState;

public record HeroActivityView(
    UUID heroId,
    UUID areaId,
    HeroActivityState state,
    Long startedAt,
    ActivityPayloadView payload) {

  public static HeroActivityView from(HeroActivity activity) {
    return switch (activity.state()) {
      case IN_ENCOUNTER -> fromEncounter(activity);

      case GHOST_TRAVELING,
          GHOST_RESURRECTING ->
        fromGhost(activity);

      default -> defaultView(activity);
    };
  }

  private static HeroActivityView fromEncounter(HeroActivity activity) {
    var encounter = (EncounterActivity) activity.payload()
        .orElseThrow(() -> new IllegalStateException(
            "Encounter payload missing for activity: " + activity));

    return new HeroActivityView(
        activity.heroId(),
        activity.areaId(),
        activity.state(),
        activity.startedAt(),
        new EncounterPayloadView(encounter.getEncounterId()));
  }

  private static HeroActivityView fromGhost(HeroActivity activity) {
    var ghost = (GhostActivity) activity.payload()
        .orElseThrow(() -> new IllegalStateException(
            "Ghost payload missing for activity: " + activity));

    return new HeroActivityView(
        activity.heroId(),
        activity.areaId(),
        activity.state(),
        activity.startedAt(),
        new GhostPayloadView(
            ghost.getCurrentAreaId(),
            ghost.getTravelDestinationId(),
            ghost.getArrivalAt(),
            ghost.getResurrectionEndAt()));
  }

  private static HeroActivityView defaultView(HeroActivity activity) {
    return new HeroActivityView(
        activity.heroId(),
        activity.areaId(),
        activity.state(),
        activity.startedAt(),
        null);
  }

  public sealed interface ActivityPayloadView
      permits EncounterPayloadView, GhostPayloadView {
  }

  public record EncounterPayloadView(
      UUID encounterId)
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
