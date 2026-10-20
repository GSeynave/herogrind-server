package com.shock.herogrind.world.internal.application;

/**
 * Initial server-side simulation values, expressed in normalized area units per world tick.
 *
 * <p>They are intentionally isolated so gameplay balancing does not spread unexplained literals
 * through simulation code.</p>
 */
final class WorldActorTuning {

  static final double HERO_ATTACK_RANGE = 0.05;
  static final double HERO_AGGRO_RANGE = 0.20;
  static final double HERO_MOVEMENT_SPEED_PER_TICK = 0.12;

  static final double MONSTER_ATTACK_RANGE = 0.05;
  static final double MONSTER_AGGRO_RANGE = 0.20;
  static final double MONSTER_MOVEMENT_SPEED_PER_TICK = 0.06;

  static final long HERO_ATTACK_COOLDOWN_MILLIS = 2_000;
  static final long MONSTER_ATTACK_COOLDOWN_MILLIS = 3_000;

  private WorldActorTuning() {
  }
}
