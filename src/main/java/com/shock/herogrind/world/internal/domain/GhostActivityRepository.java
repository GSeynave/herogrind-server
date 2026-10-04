package com.shock.herogrind.world.internal.domain;

import java.util.Optional;
import java.util.UUID;

public interface GhostActivityRepository {
  Optional<GhostActivity> findByHeroId(UUID heroId);

  void save(GhostActivity activity);

  void deleteByHeroId(UUID heroId);
}
