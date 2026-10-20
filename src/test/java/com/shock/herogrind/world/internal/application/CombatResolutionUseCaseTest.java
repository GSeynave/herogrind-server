package com.shock.herogrind.world.internal.application;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import com.shock.herogrind.world.internal.domain.ActorType;
import com.shock.herogrind.world.internal.domain.InMemoryWorldActorRepository;
import com.shock.herogrind.world.internal.domain.WorldActor;
import com.shock.herogrind.world.internal.domain.WorldActorBehavior;
import com.shock.herogrind.world.internal.domain.WorldPosition;

class CombatResolutionUseCaseTest {

  @Test
  void aggregatesSimultaneousDamageFromMultipleActors() {
    var actorRepository = new InMemoryWorldActorRepository();
    var areaId = UUID.randomUUID();
    var target = actor(UUID.randomUUID(), ActorType.MONSTER, areaId, 10D, 0D, null);
    var firstAttacker = actor(UUID.randomUUID(), ActorType.HERO, areaId, 50D, 3D, target.id());
    var secondAttacker = actor(UUID.randomUUID(), ActorType.HERO, areaId, 50D, 4D, target.id());
    var useCase = new CombatResolutionUseCase(actorRepository);

    var actions = useCase.resolve(List.of(target, firstAttacker, secondAttacker), 1_000L);

    assertEquals(2, actions.size());
    assertEquals(3D, actorRepository.findById(target.id()).orElseThrow().health());
    assertEquals(3_000L, actorRepository.findById(firstAttacker.id()).orElseThrow().nextAttackAt());
    assertEquals(3_000L, actorRepository.findById(secondAttacker.id()).orElseThrow().nextAttackAt());
  }

  @Test
  void appliesDamageAndCooldownWhenAnActorAttacksAndIsAttackedInTheSameTick() {
    var actorRepository = new InMemoryWorldActorRepository();
    var areaId = UUID.randomUUID();
    var hero = actor(UUID.randomUUID(), ActorType.HERO, areaId, 10D, 3D, null);
    var monster = actor(UUID.randomUUID(), ActorType.MONSTER, areaId, 10D, 2D, hero.id());
    hero = actor(hero.id(), ActorType.HERO, areaId, 10D, 3D, monster.id());
    var useCase = new CombatResolutionUseCase(actorRepository);

    useCase.resolve(List.of(hero, monster), 1_000L);

    assertEquals(8D, actorRepository.findById(hero.id()).orElseThrow().health());
    assertEquals(7D, actorRepository.findById(monster.id()).orElseThrow().health());
    assertEquals(3_000L, actorRepository.findById(hero.id()).orElseThrow().nextAttackAt());
    assertEquals(4_000L, actorRepository.findById(monster.id()).orElseThrow().nextAttackAt());
  }

  private WorldActor actor(
      UUID id,
      ActorType type,
      UUID areaId,
      double health,
      double attackDamage,
      UUID targetId) {
    return new WorldActor(
        id, type, areaId, new WorldPosition(0.1, 0.1), new WorldPosition(0.1, 0.1),
        WorldActorBehavior.ATTACKING, targetId,
        health, health, attackDamage, 0.05, 0.2, 0.06, 0);
  }
}
