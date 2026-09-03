package com.shock.herogrind.combat.api;

import java.util.List;
import java.util.UUID;

public record EncounterInfo(
        UUID encounterId,
        UUID heroId,
        Double heroHealth,
        UUID enemyId,
        Double enemyHealth,
        EncounterStatusInfo status,
        List<CombatActionInfo> actions,
        Long nextResolutionAt
) {

    public boolean isReadyForResolution(){
        return nextResolutionAt != null && System.currentTimeMillis() >= nextResolutionAt;

    }
}
