package com.shock.herogrind.world.internal.application;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import com.shock.herogrind.world.internal.domain.ActorType;
import com.shock.herogrind.world.internal.domain.WorldActor;
import com.shock.herogrind.world.internal.domain.WorldActorBehavior;
import com.shock.herogrind.world.internal.domain.WorldPosition;

class AttackingActorBehaviorHandlerTest {

  private final AttackingActorBehaviorHandler handler = new AttackingActorBehaviorHandler();

  @Test
  void remainsAttackingAValidTargetWithinAttackRange() {
    var areaId = UUID.randomUUID();
    var target = actor(UUID.randomUUID(), ActorType.HERO, areaId, new WorldPosition(0.14, 0.1), null);
    var attacker = actor(UUID.randomUUID(), ActorType.MONSTER, areaId, new WorldPosition(0.1, 0.1), target.id());

    var updatedActor = handler.handle(attacker, List.of(attacker, target));

    assertEquals(WorldActorBehavior.ATTACKING, updatedActor.behavior());
    assertEquals(target.id(), updatedActor.targetId());
  }

  @Test
  void resumesPursuitWhenTheTargetLeavesAttackRange() {
    var areaId = UUID.randomUUID();
    var target = actor(UUID.randomUUID(), ActorType.HERO, areaId, new WorldPosition(0.3, 0.1), null);
    var attacker = actor(UUID.randomUUID(), ActorType.MONSTER, areaId, new WorldPosition(0.1, 0.1), target.id());

    var updatedActor = handler.handle(attacker, List.of(attacker, target));

    assertEquals(WorldActorBehavior.PURSUING, updatedActor.behavior());
    assertEquals(target.id(), updatedActor.targetId());
  }

  @Test
  void resumesRoamingWhenTheTargetIsUnavailable() {
    var attacker = actor(
        UUID.randomUUID(), ActorType.MONSTER, UUID.randomUUID(), new WorldPosition(0.1, 0.1), UUID.randomUUID());

    var updatedActor = handler.handle(attacker, List.of(attacker));

    assertEquals(WorldActorBehavior.ROAMING, updatedActor.behavior());
    assertNull(updatedActor.targetId());
  }

  private WorldActor actor(
      UUID id,
      ActorType type,
      UUID areaId,
      WorldPosition position,
      UUID targetId) {
    return new WorldActor(
        id, type, areaId, position, position, WorldActorBehavior.ATTACKING, targetId,
        50D, 50D, 8D, 0.05, 0.2, 0.06, 0);
  }
}
