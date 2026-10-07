package com.shock.herogrind.world.infrastructure.persistence;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Repository;

import com.shock.herogrind.world.internal.domain.GhostActivity;
import com.shock.herogrind.world.internal.domain.GhostActivityRepository;

@Repository
public class InMemoryGhostActivityRepository implements GhostActivityRepository {

  private final Map<UUID, GhostActivity> ghostActivityStorage = new HashMap<>();

  @Override
  public void save(GhostActivity activity) {
    ghostActivityStorage.put(activity.getHeroId(), activity);
  }

  @Override
  public Optional<GhostActivity> findByHeroId(UUID heroId) {
    return Optional.ofNullable(ghostActivityStorage.get(heroId));
  }

  @Override
  public void deleteByHeroId(UUID heroId) {
    ghostActivityStorage.remove(heroId);
  }

  @Override
  public List<GhostActivity> findAll() {
    return new ArrayList<>(ghostActivityStorage.values());
  }

}
