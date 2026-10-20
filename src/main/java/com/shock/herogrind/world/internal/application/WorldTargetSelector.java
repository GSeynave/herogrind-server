package com.shock.herogrind.world.internal.application;

import java.util.List;
import java.util.Optional;

import com.shock.herogrind.world.internal.domain.WorldActor;

public interface WorldTargetSelector {

  Optional<WorldActor> select(List<WorldActor> candidates);
}
