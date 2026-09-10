package com.shock.herogrind.combat.api.api;

import java.util.UUID;

public interface CombatFacade {

    EncounterInfo startEncounter(UUID heroId, UUID areaId);
    EncounterInfo advanceEncounter(UUID encounterId);

}
