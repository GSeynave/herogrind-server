package com.shock.herogrind.area.api.web.dto;

import java.util.UUID;

import com.shock.herogrind.area.internal.application.AreaView;

public record AreaDto(UUID id, String name, AreaPositionDto position, AreaSizeDto size) {

  public static AreaDto from(AreaView view) {
    return new AreaDto(
        view.id(),
        view.name(),
        new AreaPositionDto(view.mapPosition().x(), view.mapPosition().y()),
        new AreaSizeDto(view.size().width(), view.size().height()));
  }

}
