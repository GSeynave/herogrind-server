package com.shock.herogrind.world.internal.application;

import com.shock.herogrind.world.internal.domain.WorldPosition;

public interface RandomWorldPositionSource {

  WorldPosition nextAreaPosition();
}
