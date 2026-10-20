package com.shock.herogrind.world.internal.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.UUID;

import org.junit.jupiter.api.Test;

class WorldActorTest {

  @Test
  void canAggroAnEligibleOpponentWithinAggroRange() {
    var areaId = UUID.randomUUID();
    var actor = actor(ActorType.HERO, areaId, new WorldPosition(0.1, 0.1), WorldActorBehavior.ROAMING);
    var candidate = actor(ActorType.MONSTER, areaId, new WorldPosition(0.25, 0.1), WorldActorBehavior.ROAMING);

    assertTrue(actor.canAggro(candidate));
    assertFalse(actor.canAttack(candidate));
  }

  @Test
  void cannotAggroAnActorInAnotherAreaOrAnIneligibleState() {
    var areaId = UUID.randomUUID();
    var actor = actor(ActorType.HERO, areaId, new WorldPosition(0.1, 0.1), WorldActorBehavior.ROAMING);
    var differentAreaMonster = actor(
        ActorType.MONSTER, UUID.randomUUID(), new WorldPosition(0.1, 0.1), WorldActorBehavior.ROAMING);
    var ghostMonster = actor(
        ActorType.MONSTER, areaId, new WorldPosition(0.1, 0.1), WorldActorBehavior.GHOST_TRAVELING);

    assertFalse(actor.canAggro(differentAreaMonster));
    assertFalse(actor.canAggro(ghostMonster));
  }

  @Test
  void resurrectsAtFullHealthInTheResurrectionArea() {
    var actor = actor(
        ActorType.HERO,
        UUID.randomUUID(),
        new WorldPosition(0.5, 0.5),
        WorldActorBehavior.ROAMING)
            .receiveDamage(50D)
            .withActivity(UUID.randomUUID(), WorldActorBehavior.GHOST_RESURRECTING);
    var resurrectionAreaId = UUID.randomUUID();

    var resurrected = actor.resurrect(resurrectionAreaId);

    assertEquals(resurrectionAreaId, resurrected.areaId());
    assertEquals(resurrected.maxHealth(), resurrected.health());
    assertEquals(WorldActorBehavior.ROAMING, resurrected.behavior());
    assertNull(resurrected.targetId());
  }

  @Test
  void canCounterAttackAnActorThatIsAttackingItWithinRange() {
    var areaId = UUID.randomUUID();
    var actor = actor(ActorType.HERO, areaId, new WorldPosition(0.1, 0.1), WorldActorBehavior.ROAMING);
    var attacker = new WorldActor(
        UUID.randomUUID(), ActorType.MONSTER, areaId, new WorldPosition(0.14, 0.1), new WorldPosition(0.14, 0.1),
        WorldActorBehavior.ATTACKING, actor.id(),
        50D, 50D, 8D, 0.05, 0.2, 0.06, 0);

    assertTrue(actor.canCounterAttack(attacker));
  }

  private WorldActor actor(
      ActorType type,
      UUID areaId,
      WorldPosition position,
      WorldActorBehavior behavior) {
    return new WorldActor(
        UUID.randomUUID(), type, areaId, position, position, behavior, null,
        50D, 50D, 8D, 0.05, 0.2, 0.12, 0);
  }
}
