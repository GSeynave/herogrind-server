package com.shock.herogrind.world.internal.domain;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Repository;

@Repository
public class InMemoryWorldActorRepository implements WorldActorRepository {

  private final Map<UUID, WorldActor> actors = new ConcurrentHashMap<>();

  @Override
  public void save(WorldActor actor) {
    actors.put(actor.id(), actor);
  }

  @Override
  public Optional<WorldActor> findById(UUID actorId) {
    return Optional.ofNullable(actors.get(actorId));
  }

  @Override
  public List<WorldActor> findAll() {
    return actors.values().stream().toList();
  }

  @Override
  public List<WorldActor> findAllByType(ActorType actorType) {
    return actors.values().stream()
        .filter(actor -> actor.type() == actorType)
        .toList();
  }

  @Override
  public void deleteById(UUID actorId) {
    actors.remove(actorId);
  }
}
