package com.shock.herogrind.combat.internal.infrastructure.persistence;

import com.shock.herogrind.combat.internal.application.EncounterRepository;
import com.shock.herogrind.combat.internal.domain.Encounter;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

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
}
