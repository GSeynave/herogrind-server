package com.shock.herogrind.world.internal.domain;

import java.util.UUID;

/**
 * Runtime state shared by every living entity that can move and fight in the world.
 *
 * <p>This state does not replace the hero or monster domain models. It records only their
 * simulation-specific state and is owned by the world module.</p>
 */
public record WorldActor(
    UUID id,
    ActorType type,
    UUID areaId,
    WorldPosition position,
    WorldPosition roamingDestination,
    WorldActorBehavior behavior,
    UUID targetId,
    double health,
    double maxHealth,
    double attackDamage,
    double attackRange,
    double aggroRange,
    double movementSpeed,
    long nextAttackAt) {

  public WorldActor {
    requirePositive(maxHealth, "maxHealth");
    requireNonNegative(health, "health");
    requireNonNegative(attackDamage, "attackDamage");
    requireNonNegative(attackRange, "attackRange");
    requireNonNegative(aggroRange, "aggroRange");
    requireNonNegative(movementSpeed, "movementSpeed");
    if (health > maxHealth) {
      throw new IllegalArgumentException("health must not exceed maxHealth");
    }
  }

  private static void requirePositive(double value, String field) {
    if (!Double.isFinite(value) || value <= 0) {
      throw new IllegalArgumentException(field + " must be a positive finite value");
    }
  }

  private static void requireNonNegative(double value, String field) {
    if (!Double.isFinite(value) || value < 0) {
      throw new IllegalArgumentException(field + " must be a non-negative finite value");
    }
  }

  public WorldActor withActivity(UUID nextAreaId, WorldActorBehavior nextBehavior) {
    var nextTargetId = nextBehavior == WorldActorBehavior.PURSUING
        || nextBehavior == WorldActorBehavior.ATTACKING
            ? targetId
            : null;
    return new WorldActor(
        id,
        type,
        nextAreaId,
        position,
        roamingDestination,
        nextBehavior,
        nextTargetId,
        health,
        maxHealth,
        attackDamage,
        attackRange,
        aggroRange,
        movementSpeed,
        nextAttackAt);
  }

  public WorldActor withRoamingPosition(
      WorldPosition nextPosition,
      WorldPosition nextRoamingDestination) {
    return new WorldActor(
        id,
        type,
        areaId,
        nextPosition,
        nextRoamingDestination,
        behavior,
        targetId,
        health,
        maxHealth,
        attackDamage,
        attackRange,
        aggroRange,
        movementSpeed,
        nextAttackAt);
  }

  public WorldActor pursue(UUID nextTargetId) {
    return new WorldActor(
        id,
        type,
        areaId,
        position,
        roamingDestination,
        WorldActorBehavior.PURSUING,
        nextTargetId,
        health,
        maxHealth,
        attackDamage,
        attackRange,
        aggroRange,
        movementSpeed,
        nextAttackAt);
  }

  public boolean canAggro(WorldActor candidate) {
    return isEligibleOpponent(candidate)
        && WorldSpatialRules.isWithinRange(position, candidate.position(), aggroRange);
  }

  public boolean canPursue(WorldActor candidate) {
    return isEligibleOpponent(candidate);
  }

  public boolean canAttack(WorldActor candidate) {
    return isEligibleOpponent(candidate)
        && WorldSpatialRules.isWithinRange(position, candidate.position(), attackRange);
  }

  public boolean canCounterAttack(WorldActor candidate) {
    return id.equals(candidate.targetId())
        && candidate.behavior() == WorldActorBehavior.ATTACKING
        && canAttack(candidate);
  }

  public WorldActor withPursuitPosition(WorldPosition nextPosition) {
    return new WorldActor(
        id,
        type,
        areaId,
        nextPosition,
        roamingDestination,
        WorldActorBehavior.PURSUING,
        targetId,
        health,
        maxHealth,
        attackDamage,
        attackRange,
        aggroRange,
        movementSpeed,
        nextAttackAt);
  }

  public WorldActor attackTarget() {
    return new WorldActor(
        id,
        type,
        areaId,
        position,
        roamingDestination,
        WorldActorBehavior.ATTACKING,
        targetId,
        health,
        maxHealth,
        attackDamage,
        attackRange,
        aggroRange,
        movementSpeed,
        nextAttackAt);
  }

  public WorldActor attack(UUID nextTargetId) {
    return new WorldActor(
        id,
        type,
        areaId,
        position,
        roamingDestination,
        WorldActorBehavior.ATTACKING,
        nextTargetId,
        health,
        maxHealth,
        attackDamage,
        attackRange,
        aggroRange,
        movementSpeed,
        nextAttackAt);
  }

  public WorldActor resumeRoaming() {
    return new WorldActor(
        id,
        type,
        areaId,
        position,
        roamingDestination,
        WorldActorBehavior.ROAMING,
        null,
        health,
        maxHealth,
        attackDamage,
        attackRange,
        aggroRange,
        movementSpeed,
        nextAttackAt);
  }

  public WorldActor withNextAttackAt(long nextAttackTime) {
    return new WorldActor(
        id, type, areaId, position, roamingDestination, behavior, targetId,
        health, maxHealth, attackDamage, attackRange, aggroRange, movementSpeed, nextAttackTime);
  }

  public WorldActor receiveDamage(double damage) {
    requireNonNegative(damage, "damage");
    var nextHealth = Math.max(0, health - damage);
    var nextBehavior = nextHealth == 0 ? WorldActorBehavior.DEAD : behavior;
    var nextTargetId = nextHealth == 0 ? null : targetId;
    return new WorldActor(
        id, type, areaId, position, roamingDestination, nextBehavior, nextTargetId,
        nextHealth, maxHealth, attackDamage, attackRange, aggroRange, movementSpeed, nextAttackAt);
  }

  public WorldActor resurrect(UUID resurrectionAreaId) {
    return new WorldActor(
        id, type, resurrectionAreaId, position, position, WorldActorBehavior.ROAMING, null,
        maxHealth, maxHealth, attackDamage, attackRange, aggroRange, movementSpeed, 0);
  }

  private boolean isEligibleOpponent(WorldActor candidate) {
    return type != candidate.type()
        && areaId.equals(candidate.areaId())
        && candidate.behavior() != WorldActorBehavior.DYING
        && candidate.behavior() != WorldActorBehavior.DEAD
        && candidate.behavior() != WorldActorBehavior.GHOST_WAITING
        && candidate.behavior() != WorldActorBehavior.GHOST_TRAVELING
        && candidate.behavior() != WorldActorBehavior.GHOST_RESURRECTING;
  }
}
