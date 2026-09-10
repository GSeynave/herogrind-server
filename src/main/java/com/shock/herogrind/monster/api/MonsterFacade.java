package com.shock.herogrind.monster.api;

import java.util.UUID;

public interface MonsterFacade {

    MonsterInfo getMonsterInfoByAreaId(UUID areaId);
    MonsterInfo getMonsterInfoById(UUID monsterId);
}
