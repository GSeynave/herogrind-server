package com.shock.herogrind.combat.api;

import java.util.UUID;

public interface CombatFacade {

    EncounterInfo startEncounter(UUID heroId, UUID areaId);
    EncounterInfo getEncounterById(UUID encounterId);
    EncounterInfo advanceEncounter(UUID encounterId);

}
