package com.shock.herogrind.area.internal.domain;

import java.util.UUID;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Area {

  private UUID id;
  private String name;
  private Boolean unlocked;
  private AreaState state;
  private MapPosition mapPosition;
  private Size size;

  public Boolean isUnlocked() {
    return this.unlocked;
  }
}
