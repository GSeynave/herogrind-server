package com.shock.herogrind.combat.internal.application;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.shock.herogrind.combat.internal.domain.Encounter;

public interface EncounterRepository {

  void save(Encounter encounter);

  Encounter findById(UUID encounterId);

  Optional<Encounter> findByHeroId(UUID heroId);

  void deleteById(UUID encounterId);

  List<Encounter> findAll();
}
