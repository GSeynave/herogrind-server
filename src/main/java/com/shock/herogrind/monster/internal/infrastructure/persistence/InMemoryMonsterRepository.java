package com.shock.herogrind.monster.internal.infrastructure.persistence;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.stereotype.Component;

import com.shock.herogrind.monster.internal.application.MonsterRepository;
import com.shock.herogrind.monster.internal.domain.Monster;

@Component
public class InMemoryMonsterRepository implements MonsterRepository {
  // later i'll inject the jpa repository interface.
  Map<UUID, Monster> monsters = new HashMap<>();

  @Override
  public void save(Monster monster) {
    this.monsters.put(monster.id(), monster);
  }

  @Override
  public List<Monster> findAllByAreaId(UUID areaId) {
    return monsters.values().stream().filter(m -> m.areaId().equals(areaId)).toList();
  }

  @Override
  public Monster findById(UUID monsterId) {
    return monsters.get(monsterId);
  }

  @Override
  public List<Monster> findAll() {
    return monsters.values().stream().toList();
  }

}
