package com.shock.herogrind.monster.internal.application;

import com.shock.herogrind.monster.api.MonsterFacade;
import com.shock.herogrind.monster.api.MonsterInfo;

import java.util.UUID;

public class MonsterFacadeImpl implements MonsterFacade {

    @Override
    public MonsterInfo getMonsterInfoByAreaId(UUID areaId) {
        return new MonsterInfo(UUID.randomUUID(),"Goblin", 10D, 2D);
    }

    @Override
    public MonsterInfo getMonsterInfoById(UUID monsterId) {
        return new MonsterInfo(UUID.randomUUID(),"Goblin", 10D, 2D);
    }
}
