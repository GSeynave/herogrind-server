package com.shock.herogrind.monster.internal.application;

import java.util.List;
import java.util.UUID;

import com.shock.herogrind.monster.internal.domain.Monster;

public interface MonsterRepository {

  void save(Monster monster);

  Monster findById(UUID monsterId);

  List<Monster> findAllByAreaId(UUID areaId);

  List<Monster> findAll();
}
