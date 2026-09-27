package com.shock.herogrind.world.internal.domain;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.UUID;

import com.shock.herogrind.combat.api.EncounterInfo;
import com.shock.herogrind.combat.api.GhostInfo;

public record HeroActivity(
    UUID heroId,
    UUID areaId,
    HeroActivityState state,
    Long startedAt,
    Long nextResolutionAt,
    Optional<HeroActivityPayload> payload) {

  public static HeroActivity idle(UUID heroId) {
    var now = Instant.now();
    return new HeroActivity(heroId,
        null,
        HeroActivityState.IDLE,
        now.toEpochMilli(),
        now.plus(0, ChronoUnit.SECONDS).toEpochMilli(),
        Optional.empty());
  }

  public static HeroActivity inDungeon(UUID heroId) {
    var now = Instant.now();
    return new HeroActivity(heroId,
        null,
        HeroActivityState.DUNGEON,
        now.toEpochMilli(),
        now.plus(3, ChronoUnit.SECONDS).toEpochMilli(),
        Optional.empty());
  }

  public static HeroActivity inEncounter(UUID heroId, UUID areaId, EncounterInfo encounterInfo) {
    var now = Instant.now();
    return new HeroActivity(heroId,
        areaId,
        HeroActivityState.IN_ENCOUNTER,
        now.toEpochMilli(),
        now.plus(3, ChronoUnit.SECONDS).toEpochMilli(),
        Optional.of(EncounterActivity.fromEncounterInfo(encounterInfo)));

  }

  public static HeroActivity roaming(UUID heroId, UUID areaId) {
    var now = Instant.now();
    return new HeroActivity(heroId,
        areaId,
        HeroActivityState.ROAMING,
        now.toEpochMilli(),
        now.plus(2, ChronoUnit.SECONDS).toEpochMilli(),
        Optional.empty());
  }

  public static HeroActivity ghostResurrecting(UUID heroId, UUID areaId, GhostActivity ghostActivity) {
    var now = Instant.now();
    return new HeroActivity(heroId,
        areaId,
        HeroActivityState.GHOST_RESURRECTING,
        now.toEpochMilli(),
        ghostActivity.getResurrectionEndAt(),
        Optional.of(ghostActivity));
  }

  public static HeroActivity ghostTraveling(UUID heroId, UUID areaId, GhostActivity ghostActivity) {
    var now = Instant.now();
    return new HeroActivity(heroId,
        areaId,
        HeroActivityState.GHOST_TRAVELING,
        now.toEpochMilli(),
        ghostActivity.getArrivalAt(),
        Optional.of(ghostActivity));
  }

  public static HeroActivity ghostWaiting(UUID heroId, UUID areaId, GhostInfo ghostInfo) {
    var now = Instant.now();
    return new HeroActivity(heroId,
        areaId,
        HeroActivityState.GHOST_WAITING,
        now.toEpochMilli(),
        now.plus(2, ChronoUnit.SECONDS).toEpochMilli(),
        Optional.empty());
  }

  public static HeroActivity dead(UUID heroId, UUID areaId) {
    var now = Instant.now();
    return new HeroActivity(heroId,
        areaId,
        HeroActivityState.DEAD,
        now.toEpochMilli(),
        now.plus(3, ChronoUnit.SECONDS).toEpochMilli(),
        Optional.empty());
  }

  public static HeroActivity dying(UUID heroId, UUID areaId) {
    var now = Instant.now();
    return new HeroActivity(heroId,
        areaId,
        HeroActivityState.DYING,
        now.toEpochMilli(),
        now.plus(3, ChronoUnit.SECONDS).toEpochMilli(),
        Optional.empty());
  }

  public Boolean isReadyForNextActivity() {
    return Instant.now().toEpochMilli() >= this.nextResolutionAt;
  }

  public Boolean isGhost() {
    return this.state.equals(HeroActivityState.GHOST_WAITING)
        || this.state.equals(HeroActivityState.GHOST_TRAVELING)
        || this.state.equals(HeroActivityState.GHOST_RESURRECTING);
  }

  public String log() {
    return String.format("Hero {%s} is currently in {%s}", heroId, state());
  }
}
