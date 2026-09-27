package com.shock.herogrind.area.api;

import java.util.UUID;

import com.shock.herogrind.area.internal.application.AreaView;

public record AreaInfo(UUID id, String name, Boolean isUnlocked) {

  public static AreaInfo from(AreaView area) {
    return new AreaInfo(area.id(), area.name(), area.isUnlocked());
  }

  public boolean isTown() {
    return this.name.equalsIgnoreCase("Town");
  }

  public boolean isGraveyard() {
    return this.name.equalsIgnoreCase("Graveyard");
  }
}
