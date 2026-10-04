package com.shock.herogrind.world.internal.domain;

import java.time.Instant;
import java.util.UUID;

public record HeroActivity(
    UUID heroId,
    UUID areaId,
    HeroActivityState state,
    Long startedAt,
    Long nextResolutionAt) {

  public Boolean isReadyForNextActivity() {
    return Instant.now().toEpochMilli() >= this.nextResolutionAt;
  }

  public Boolean isGhost() {
    return this.state.equals(HeroActivityState.GHOST_WAITING)
        || this.state.equals(HeroActivityState.GHOST_TRAVELING)
        || this.state.equals(HeroActivityState.GHOST_RESURRECTING);
  }

  public String log() {
    return String.format("Hero {%s} is currently in {%s}, ", heroId, state());
  }
}
