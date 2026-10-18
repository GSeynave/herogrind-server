package com.shock.herogrind.world.api.web.dto;

import java.util.UUID;

import com.shock.herogrind.world.internal.application.HeroActivityView;
import com.shock.herogrind.world.internal.domain.HeroActivityState;

public record HeroActivityDto(
    UUID heroId,
    UUID areaId,
    HeroActivityState state,
    Long startedAt) {

  public static HeroActivityDto from(HeroActivityView view) {
    return new HeroActivityDto(
        view.heroId(),
        view.areaId(),
        view.state(),
        view.startedAt());
  }
}
