package com.shock.herogrind.area.api.dto;

import java.util.UUID;

import com.shock.herogrind.area.internal.application.AreaView;
import com.shock.herogrind.area.internal.domain.MapPosition;
import com.shock.herogrind.area.internal.domain.Size;

public record AreaDto(UUID id, String name, MapPosition position, Size size) {

  public static AreaDto from(AreaView view) {
    return new AreaDto(view.id(), view.name(), view.mapPosition(), view.size());
  }

}
