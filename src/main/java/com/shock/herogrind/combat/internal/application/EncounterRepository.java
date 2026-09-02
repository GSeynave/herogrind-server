package com.shock.herogrind.combat.internal.application;

import com.shock.herogrind.combat.internal.domain.Encounter;

import java.util.UUID;

public interface EncounterRepository {

    void save(Encounter encounter);
    Encounter findById(UUID encounterId);
}
