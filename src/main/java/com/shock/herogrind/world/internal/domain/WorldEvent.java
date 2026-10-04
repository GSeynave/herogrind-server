package com.shock.herogrind.world.internal.domain;

import com.shock.herogrind.combat.api.CombatActionInfo;

public record WorldEvent(
    WorldEventType eventType,
    WorldEventPayload payload,
    Long occurredAt) {
  public static WorldEvent from(HeroActivity activity) {
    if (activity.state().equals(HeroActivityState.IN_ENCOUNTER)) {
      return encounterStartedEvent((EncounterActivity) activity.payload().orElseThrow());
    }
    return new WorldEvent(getType(activity.state()), new HeroActivityEvent(activity.heroId(), activity.areaId()),
        System.currentTimeMillis());
  }

  private static WorldEvent encounterStartedEvent(EncounterActivity encounter) {
    return new WorldEvent(WorldEventType.HERO_STARTED_ENCOUNTER,
        new EncounterEvent(
            encounter.getEncounterId(),
            encounter.getHeroId(),
            encounter.getHeroHealth(),
            encounter.getEnemyId(),
            encounter.getEnemyHealth()),
        System.currentTimeMillis());
  }

  public static WorldEvent from(CombatActionInfo action) {
    return new WorldEvent(WorldEventType.COMBAT_ACTION, new CombatActionEvent(action.sourceId(), action.targetId(),
        action.targetHealth(), action.type(), action.value()), System.currentTimeMillis());
  }

  public static WorldEvent from(GhostActivity ghostActivity) {
    return new WorldEvent(getType(ghostActivity.getState()),
        new GhostEvent(ghostActivity.getHeroId(),
            ghostActivity.getCurrentAreaId(),
            GhostState.from(ghostActivity.getState()),
            ghostActivity.getCurrentAreaId(),
            ghostActivity.getTravelDestinationId(),
            ghostActivity.getArrivalAt(),
            ghostActivity.getResurrectionEndAt()),
        System.currentTimeMillis());
  }

  private static WorldEventType getType(HeroActivityState state) {
    return switch (state) {
      case IDLE -> WorldEventType.HERO_IDLE;
      case DUNGEON -> WorldEventType.HERO_ENTERED_DUNGEON;
      case ROAMING -> WorldEventType.HERO_STARTED_ROAMING;
      case IN_ENCOUNTER -> WorldEventType.HERO_STARTED_ENCOUNTER;
      case DYING -> WorldEventType.HERO_DIED;
      case GHOST_RESURRECTING -> WorldEventType.HERO_GHOST_RESURRECTING;
      case GHOST_TRAVELING -> WorldEventType.HERO_GHOST_TRAVELING;

      default -> throw new IllegalArgumentException("Unknown state: " + state);
    };
  }
}
