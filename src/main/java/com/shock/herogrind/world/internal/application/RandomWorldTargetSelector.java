package com.shock.herogrind.world.internal.application;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

import org.springframework.stereotype.Component;

import com.shock.herogrind.world.internal.domain.WorldActor;

@Component
public class RandomWorldTargetSelector implements WorldTargetSelector {

  @Override
  public Optional<WorldActor> select(List<WorldActor> candidates) {
    if (candidates.isEmpty()) {
      return Optional.empty();
    }
    return Optional.of(candidates.get(ThreadLocalRandom.current().nextInt(candidates.size())));
  }
}
