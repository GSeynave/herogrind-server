package com.shock.herogrind.combat.api.api;

import com.shock.herogrind.combat.api.EncounterStatusInfo;

import java.util.List;
import java.util.UUID;

public record EncounterInfo(
        UUID encounterId,
        UUID heroId,
        UUID enemyId,
        Double heroHealth,
        Double enemyHealth,
        EncounterStatusInfo status,
        List<String> actions
) {

}
