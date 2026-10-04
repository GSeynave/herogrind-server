package com.shock.herogrind.world.internal.domain;

public enum GhostState {
  WAITING,
  TRAVELING,
  RESURRECTING;

  public static GhostState from(HeroActivityState state) {
    return switch (state) {
      case GHOST_WAITING -> WAITING;
      case GHOST_TRAVELING -> TRAVELING;
      case GHOST_RESURRECTING -> RESURRECTING;
      default -> throw new IllegalArgumentException("Unknown state: " + state);
    };
  }
}
