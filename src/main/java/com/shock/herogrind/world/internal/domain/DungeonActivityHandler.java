package com.shock.herogrind.world.internal.domain;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Collections;

import org.springframework.stereotype.Component;

import com.shock.herogrind.world.internal.application.ActivityHandler;
import com.shock.herogrind.world.internal.application.ActivityHandlerResult;

@Component
public class DungeonActivityHandler implements ActivityHandler {

  @Override
  public boolean supports(HeroActivityState state) {
    return state == HeroActivityState.DUNGEON;
  }

  @Override
  public ActivityHandlerResult handle(HeroActivity currentActivity) {
    if (!currentActivity.isReadyForNextActivity()) {
      return ActivityHandlerResult.empty();
    }
    var now = Instant.now();
    var nextActivity = new HeroActivity(
        currentActivity.heroId(),
        null,
        HeroActivityState.DUNGEON,
        now.toEpochMilli(),
        now.plus(3, ChronoUnit.SECONDS).toEpochMilli());
    return new ActivityHandlerResult(
        nextActivity,
        Collections.emptyList());
  }
}
