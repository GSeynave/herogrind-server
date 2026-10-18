package com.shock.herogrind.monster.api.web.dto;

import java.util.UUID;

import com.shock.herogrind.monster.api.MonsterInfo;

public record MonsterDto(
    UUID id,
    String name,
    Double health,
    Double attackDamage) {

  public static MonsterDto from(MonsterInfo monster) {
    return new MonsterDto(monster.id(), monster.name(), monster.health(), monster.attackDamage());
  }
}
