package com.shock.herogrind.combat.internal.infrastructure.persistence;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Repository;

import com.shock.herogrind.combat.internal.application.EncounterRepository;
import com.shock.herogrind.combat.internal.domain.Encounter;

@Repository
public class InMemeryEncounterRepository implements EncounterRepository {

  private final Map<UUID, Encounter> encounterStorage = new HashMap<>();

  @Override
  public void save(Encounter encounter) {
    // Implementation for saving the encounter in memory
    encounterStorage.put(encounter.id(), encounter);
  }

  @Override
  public Encounter findById(UUID encounterId) {
    // Implementation for finding the encounter by ID in memory
    return encounterStorage.get(encounterId);
  }

  @Override
  public Optional<Encounter> findByHeroId(UUID heroId) {
    // Implementation for finding the encounter by ID in memory
    return encounterStorage.values().stream()
        .filter(encounter -> encounter.heroId().equals(heroId))
        .findFirst();
  }

  @Override
  public void deleteById(UUID encounterId) {
    // Implementation for deleting the encounter by ID in memory
    encounterStorage.remove(encounterId);
  }
}
