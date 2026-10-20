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

class PursuingActorBehaviorHandlerTest {

  private final PursuingActorBehaviorHandler handler = new PursuingActorBehaviorHandler();

  @Test
  void movesTowardsAValidTargetThatIsOutsideAttackRange() {
    var areaId = UUID.randomUUID();
    var target = actor(UUID.randomUUID(), ActorType.HERO, areaId, new WorldPosition(0.8, 0.1), null);
    var pursuer = actor(UUID.randomUUID(), ActorType.MONSTER, areaId, new WorldPosition(0.1, 0.1), target.id());

    var updatedActor = handler.handle(pursuer, List.of(pursuer, target));

    assertEquals(WorldActorBehavior.PURSUING, updatedActor.behavior());
    assertEquals(new WorldPosition(0.16, 0.1), updatedActor.position());
  }

  @Test
  void startsAttackingWhenWithinItsOwnAttackRange() {
    var areaId = UUID.randomUUID();
    var target = actor(UUID.randomUUID(), ActorType.HERO, areaId, new WorldPosition(0.14, 0.1), null);
    var pursuer = actor(UUID.randomUUID(), ActorType.MONSTER, areaId, new WorldPosition(0.1, 0.1), target.id());

    var updatedActor = handler.handle(pursuer, List.of(pursuer, target));

    assertEquals(WorldActorBehavior.ATTACKING, updatedActor.behavior());
    assertEquals(pursuer.position(), updatedActor.position());
  }

  @Test
  void resumesRoamingWhenTheTargetIsUnavailable() {
    var pursuer = actor(UUID.randomUUID(), ActorType.MONSTER, UUID.randomUUID(), new WorldPosition(0.1, 0.1), UUID.randomUUID());

    var updatedActor = handler.handle(pursuer, List.of(pursuer));

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
        id, type, areaId, position, position, WorldActorBehavior.PURSUING, targetId,
        50D, 50D, 8D, 0.05, 0.2, 0.06, 0);
  }
}
