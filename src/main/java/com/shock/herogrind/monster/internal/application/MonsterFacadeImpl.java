package com.shock.herogrind.monster.internal.application;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Component;

import com.shock.herogrind.monster.api.MonsterFacade;
import com.shock.herogrind.monster.api.MonsterInfo;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class MonsterFacadeImpl implements MonsterFacade {

  private final GetMonsterUseCase getMonsterUseCase;

  @Override
  public MonsterInfo getMonsterInfoByAreaId(UUID areaId) {
    return getMonsterUseCase.getMonsterInfoByAreaId(areaId);
  }

  @Override
  public MonsterInfo getMonsterInfoById(UUID monsterId) {
    return getMonsterUseCase.getMonsterInfoById(monsterId);
  }

  @Override
  public List<MonsterInfo> findAll() {
    return getMonsterUseCase.getAllMonsters();
  }
}
