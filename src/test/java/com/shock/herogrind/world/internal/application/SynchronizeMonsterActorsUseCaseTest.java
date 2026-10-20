package com.shock.herogrind.world.internal.application;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import com.shock.herogrind.monster.api.MonsterFacade;
import com.shock.herogrind.monster.api.MonsterInfo;
import com.shock.herogrind.monster.api.MonsterWorldInfo;
import com.shock.herogrind.world.internal.domain.ActorType;
import com.shock.herogrind.world.internal.domain.InMemoryWorldActorRepository;
import com.shock.herogrind.world.internal.domain.WorldActorBehavior;

class SynchronizeMonsterActorsUseCaseTest {

  @Test
  void createsPersistentActorsForEveryMonsterInItsAssignedArea() {
    var darkForestId = UUID.randomUUID();
    var mineId = UUID.randomUUID();
    var monsters = List.of(
        monster("Goblin", darkForestId),
        monster("Goblin", darkForestId),
        monster("Goblin", darkForestId),
        monster("Wolf", mineId),
        monster("Wolf", mineId),
        monster("Wolf", mineId));
    var actorRepository = new InMemoryWorldActorRepository();
    var useCase = new SynchronizeMonsterActorsUseCase(new FixedMonsterFacade(monsters), actorRepository);

    useCase.synchronizeMonsters();
    useCase.synchronizeMonsters();

    var actors = actorRepository.findAllByType(ActorType.MONSTER);
    assertEquals(6, actors.size());
    assertEquals(3, actors.stream().filter(actor -> actor.areaId().equals(darkForestId)).count());
    assertEquals(3, actors.stream().filter(actor -> actor.areaId().equals(mineId)).count());
    actors.forEach(actor -> {
      assertEquals(WorldActorBehavior.ROAMING, actor.behavior());
      assertTrue(actor.position().x() >= 0.1 && actor.position().x() < 0.9);
      assertTrue(actor.position().y() >= 0.1 && actor.position().y() < 0.9);
    });
  }

  private static MonsterWorldInfo monster(String name, UUID areaId) {
    return new MonsterWorldInfo(UUID.randomUUID(), name, 20D, 3D, areaId);
  }

  private static final class FixedMonsterFacade implements MonsterFacade {
    private final List<MonsterWorldInfo> monsters;

    private FixedMonsterFacade(List<MonsterWorldInfo> monsters) {
      this.monsters = monsters;
    }

    @Override
    public MonsterInfo getMonsterInfoByAreaId(UUID areaId) {
      throw new UnsupportedOperationException();
    }

    @Override
    public MonsterInfo getMonsterInfoById(UUID monsterId) {
      throw new UnsupportedOperationException();
    }

    @Override
    public List<MonsterInfo> findAll() {
      return List.of();
    }

    @Override
    public List<MonsterWorldInfo> findAllForWorld() {
      return monsters;
    }
  }
}
