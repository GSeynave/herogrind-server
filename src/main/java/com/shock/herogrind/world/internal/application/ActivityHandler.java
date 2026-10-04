package com.shock.herogrind.world.internal.application;

import com.shock.herogrind.world.internal.domain.HeroActivity;
import com.shock.herogrind.world.internal.domain.HeroActivityState;

public interface ActivityHandler {
  ActivityHandlerResult handle(HeroActivity currentActivity);

  boolean supports(HeroActivityState state);
}
