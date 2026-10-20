package com.shock.herogrind.world.internal.application;

import java.util.concurrent.ThreadLocalRandom;

import org.springframework.stereotype.Component;

import com.shock.herogrind.monster.api.MonsterFacade;
import com.shock.herogrind.world.internal.domain.ActorType;
import com.shock.herogrind.world.internal.domain.WorldActor;
import com.shock.herogrind.world.internal.domain.WorldActorBehavior;
import com.shock.herogrind.world.internal.domain.WorldActorRepository;
import com.shock.herogrind.world.internal.domain.WorldPosition;

import lombok.RequiredArgsConstructor;

/**
 * Ensures each seeded monster has one persistent actor in its assigned area.
 */
@Component
@RequiredArgsConstructor
public class SynchronizeMonsterActorsUseCase {

  private static final double SPAWN_POSITION_MARGIN = 0.1;
  private static final double SPAWN_POSITION_UPPER_BOUND = 1 - SPAWN_POSITION_MARGIN;

  private final MonsterFacade monsterFacade;
  private final WorldActorRepository worldActorRepository;

  public void synchronizeMonsters() {
    monsterFacade.findAllForWorld().forEach(monster -> worldActorRepository.findById(monster.id())
        .orElseGet(() -> createMonsterActor(monster)));
  }

  private WorldActor createMonsterActor(com.shock.herogrind.monster.api.MonsterWorldInfo monster) {
    var initialPosition = randomSpawnPosition();
    var actor = new WorldActor(
        monster.id(),
        ActorType.MONSTER,
        monster.areaId(),
        initialPosition,
        initialPosition,
        WorldActorBehavior.ROAMING,
        null,
        monster.health(),
        monster.health(),
        monster.attackDamage(),
        WorldActorTuning.MONSTER_ATTACK_RANGE,
        WorldActorTuning.MONSTER_AGGRO_RANGE,
        WorldActorTuning.MONSTER_MOVEMENT_SPEED_PER_TICK,
        0);
    worldActorRepository.save(actor);
    return actor;
  }

  private WorldPosition randomSpawnPosition() {
    var random = ThreadLocalRandom.current();
    return new WorldPosition(
        random.nextDouble(SPAWN_POSITION_MARGIN, SPAWN_POSITION_UPPER_BOUND),
        random.nextDouble(SPAWN_POSITION_MARGIN, SPAWN_POSITION_UPPER_BOUND));
  }
}
