package com.shock.herogrind.world.internal.domain;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface WorldActorRepository {

  void save(WorldActor actor);

  Optional<WorldActor> findById(UUID actorId);

  List<WorldActor> findAll();

  List<WorldActor> findAllByType(ActorType actorType);

  void deleteById(UUID actorId);
}
