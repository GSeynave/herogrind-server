package com.shock.herogrind.world.internal.application;

import java.util.List;
import java.util.Optional;

import com.shock.herogrind.world.internal.domain.HeroActivity;
import com.shock.herogrind.world.internal.domain.WorldEvent;

public class ActivityHandlerResult {
  private final Optional<HeroActivity> nextActivity;
  private final List<WorldEvent> events;

  public ActivityHandlerResult(HeroActivity nextActivity, List<WorldEvent> events) {
    this.nextActivity = Optional.ofNullable(nextActivity);
    this.events = events;
  }

  public Optional<HeroActivity> getNextActivity() {
    return nextActivity;
  }

  public List<WorldEvent> getEvents() {
    return events;
  }

  public static ActivityHandlerResult empty() {
    return new ActivityHandlerResult(null, List.of());
  }

}
