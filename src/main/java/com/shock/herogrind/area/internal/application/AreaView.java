package com.shock.herogrind.area.internal.application;

import java.util.UUID;

import com.shock.herogrind.area.internal.domain.Area;
import com.shock.herogrind.area.internal.domain.MapPosition;
import com.shock.herogrind.area.internal.domain.Size;

public record AreaView(UUID id, String name, Boolean isUnlocked, MapPosition mapPosition, Size size) {

  public static AreaView from(Area area) {
    return new AreaView(area.getId(), area.getName(), area.isUnlocked(), area.getMapPosition(), area.getSize());
  }
}
