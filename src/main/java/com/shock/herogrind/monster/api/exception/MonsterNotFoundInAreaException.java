package com.shock.herogrind.monster.api.exception;

import java.util.UUID;

public class MonsterNotFoundInAreaException extends RuntimeException {
  private final UUID areaId;

  public MonsterNotFoundInAreaException(UUID areaId) {
    this.areaId = areaId;
  }
}
