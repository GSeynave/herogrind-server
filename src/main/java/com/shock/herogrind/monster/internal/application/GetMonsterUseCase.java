package com.shock.herogrind.monster.internal.application;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Component;

import com.shock.herogrind.monster.api.MonsterInfo;
import com.shock.herogrind.monster.api.exception.MonsterNotFoundInAreaException;
import com.shock.herogrind.monster.internal.domain.Monster;

@Component
public class GetMonsterUseCase {

  private final MonsterRepository monsterRepository;

  public GetMonsterUseCase(MonsterRepository monsterRepository) {
    this.monsterRepository = monsterRepository;
  }

  public MonsterInfo getMonsterInfoById(UUID monsterId) {
    Monster monster = monsterRepository.findById(monsterId);
    return new MonsterInfo(monster.id(), monster.name(), monster.health(), monster.attackDamage());
  }

  public MonsterInfo getMonsterInfoByAreaId(UUID areaId) {
    List<Monster> monsters = monsterRepository.findAllByAreaId(areaId);
    if (monsters.isEmpty()) {
      throw new MonsterNotFoundInAreaException(areaId);
    }
    Monster monster = monsters.get(0);
    return new MonsterInfo(monster.id(), monster.name(), monster.health(), monster.attackDamage());
  }

  public List<MonsterInfo> getAllMonsters() {
    return monsterRepository.findAll().stream()
        .map(monster -> new MonsterInfo(monster.id(), monster.name(), monster.health(), monster.attackDamage()))
        .toList();
  }
}
