package com.shock.herogrind.world.internal.application;

import java.util.UUID;

import com.shock.herogrind.world.internal.domain.HeroActivity;
import com.shock.herogrind.world.internal.domain.HeroActivityState;

public record HeroActivityView(
    UUID heroId,
    UUID areaId,
    HeroActivityState state,
    UUID encounterId) {

  public static HeroActivityView from(HeroActivity activity) {
    var encounterId = activity.encounterInfo().map(encounter -> encounter.encounterId()).orElse(null);
    return new HeroActivityView(
        activity.heroId(),
        activity.areaId(),
        activity.state(),
        encounterId);
  }
}
