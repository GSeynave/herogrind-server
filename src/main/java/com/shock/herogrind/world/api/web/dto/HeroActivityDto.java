package com.shock.herogrind.world.api.web.dto;

import java.util.UUID;

import com.shock.herogrind.world.internal.application.HeroActivityView;

public record HeroActivityDto(
    UUID heroId,
    UUID areaId,
    String state,
    Long startedAt) {

  public static HeroActivityDto from(HeroActivityView view) {
    return new HeroActivityDto(
        view.heroId(),
        view.areaId(),
        view.state().name(),
        view.startedAt());
  }
}
