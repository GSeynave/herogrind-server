package com.shock.herogrind.monster.api;

import java.util.List;
import java.util.UUID;

public interface MonsterFacade {

  MonsterInfo getMonsterInfoByAreaId(UUID areaId);

  MonsterInfo getMonsterInfoById(UUID monsterId);

  List<MonsterInfo> findAll();
}
