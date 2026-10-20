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

class ActorTickUseCaseTest {

  @Test
  void advancesRoamingActorsAccordingToTheirIndividualMovementSpeeds() {
    var actorRepository = new InMemoryWorldActorRepository();
    var hero = roamingActor(ActorType.HERO, 0.12);
    var monster = roamingActor(ActorType.MONSTER, 0.06);
    actorRepository.save(hero);
    actorRepository.save(monster);
    var tick = new ActorTickUseCase(
        actorRepository,
        List.of(new RoamingActorBehaviorHandler(
            () -> new WorldPosition(0.5, 0.5),
            candidates -> java.util.Optional.empty())),
        new CombatResolutionUseCase(actorRepository));

    tick.execute();

    assertEquals(new WorldPosition(0.22, 0.1), actorRepository.findById(hero.id()).orElseThrow().position());
    assertEquals(new WorldPosition(0.16, 0.1), actorRepository.findById(monster.id()).orElseThrow().position());
  }

  @Test
  void choosesANewRandomDestinationAfterReachingThePreviousOne() {
    var actorRepository = new InMemoryWorldActorRepository();
    var actor = new WorldActor(
        UUID.randomUUID(), ActorType.MONSTER, UUID.randomUUID(), new WorldPosition(0.5, 0.5),
        new WorldPosition(0.5, 0.5), WorldActorBehavior.ROAMING, null,
        50D, 50D, 8D, 0.05, 0.2, 0.1, 0);
    actorRepository.save(actor);
    var destination = new WorldPosition(0.9, 0.5);
    var tick = new ActorTickUseCase(
        actorRepository,
        List.of(new RoamingActorBehaviorHandler(
            () -> destination,
            candidates -> java.util.Optional.empty())),
        new CombatResolutionUseCase(actorRepository));

    tick.execute();

    var updatedActor = actorRepository.findById(actor.id()).orElseThrow();
    assertEquals(destination, updatedActor.roamingDestination());
    assertEquals(new WorldPosition(0.6, 0.5), updatedActor.position());
  }

  private WorldActor roamingActor(ActorType type, double movementSpeed) {
    return new WorldActor(
        UUID.randomUUID(), type, UUID.randomUUID(), new WorldPosition(0.1, 0.1), new WorldPosition(0.9, 0.1),
        WorldActorBehavior.ROAMING, null, 50D, 50D, 8D, 0.05, 0.2, movementSpeed, 0);
  }

  @Test
  void transitionsToPursuingTheSelectedOpposingActorWithinAggroRange() {
    var actorRepository = new InMemoryWorldActorRepository();
    var source = roamingActor(ActorType.MONSTER, 0.06);
    var selectedTarget = new WorldActor(
        UUID.randomUUID(), ActorType.HERO, source.areaId(), new WorldPosition(0.2, 0.1),
        new WorldPosition(0.2, 0.1), WorldActorBehavior.ROAMING, null,
        50D, 50D, 8D, 0.05, 0.2, 0.12, 0);
    var otherAreaTarget = new WorldActor(
        UUID.randomUUID(), ActorType.HERO, UUID.randomUUID(), new WorldPosition(0.1, 0.1),
        new WorldPosition(0.1, 0.1), WorldActorBehavior.ROAMING, null,
        50D, 50D, 8D, 0.05, 0.2, 0.12, 0);
    actorRepository.save(source);
    actorRepository.save(selectedTarget);
    actorRepository.save(otherAreaTarget);
    var tick = new ActorTickUseCase(
        actorRepository,
        List.of(new RoamingActorBehaviorHandler(
            () -> new WorldPosition(0.5, 0.5),
            candidates -> candidates.isEmpty()
                ? java.util.Optional.empty()
                : java.util.Optional.of(candidates.getFirst()))),
        new CombatResolutionUseCase(actorRepository));

    tick.execute();

    var updatedSource = actorRepository.findById(source.id()).orElseThrow();
    assertEquals(WorldActorBehavior.PURSUING, updatedSource.behavior());
    assertEquals(selectedTarget.id(), updatedSource.targetId());
  }

  @Test
  void stopsRoamingToCounterAttackAnIncomingAttackerWithinRange() {
    var actorRepository = new InMemoryWorldActorRepository();
    var areaId = UUID.randomUUID();
    var hero = new WorldActor(
        UUID.randomUUID(), ActorType.HERO, areaId, new WorldPosition(0.1, 0.1), new WorldPosition(0.9, 0.1),
        WorldActorBehavior.ROAMING, null,
        50D, 50D, 8D, 0.05, 0.2, 0.12, 0);
    var monster = new WorldActor(
        UUID.randomUUID(), ActorType.MONSTER, areaId, new WorldPosition(0.14, 0.1), new WorldPosition(0.14, 0.1),
        WorldActorBehavior.ATTACKING, hero.id(),
        50D, 50D, 8D, 0.05, 0.2, 0.06, 0);
    actorRepository.save(hero);
    actorRepository.save(monster);
    var tick = new ActorTickUseCase(
        actorRepository,
        List.of(
            new RoamingActorBehaviorHandler(
                () -> new WorldPosition(0.5, 0.5),
                candidates -> java.util.Optional.empty()),
            new AttackingActorBehaviorHandler()),
        new CombatResolutionUseCase(actorRepository));

    tick.execute();

    var updatedHero = actorRepository.findById(hero.id()).orElseThrow();
    assertEquals(hero.position(), updatedHero.position());
    assertEquals(WorldActorBehavior.ATTACKING, updatedHero.behavior());
    assertEquals(monster.id(), updatedHero.targetId());
  }
}
