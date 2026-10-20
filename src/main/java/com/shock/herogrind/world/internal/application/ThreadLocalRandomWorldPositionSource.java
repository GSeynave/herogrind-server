package com.shock.herogrind.world.internal.application;

import java.util.concurrent.ThreadLocalRandom;

import org.springframework.stereotype.Component;

import com.shock.herogrind.world.internal.domain.WorldPosition;

@Component
public class ThreadLocalRandomWorldPositionSource implements RandomWorldPositionSource {

  @Override
  public WorldPosition nextAreaPosition() {
    var random = ThreadLocalRandom.current();
    return new WorldPosition(random.nextDouble(), random.nextDouble());
  }
}
