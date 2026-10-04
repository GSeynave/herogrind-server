package com.shock.herogrind.world.internal.domain;

public record WorldEvent(
    WorldEventType eventType,
    WorldEventPayload payload,
    Long occurredAt) {
  public static WorldEvent from(HeroActivity activity) {
    return new WorldEvent(getType(activity.state()), new HeroActivityEvent(activity.heroId(), activity.areaId()),
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
