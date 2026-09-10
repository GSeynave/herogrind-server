package com.shock.herogrind.combat.api.api;

import java.util.UUID;

public record CombatActionInfo(
        CombatActionType type,
        UUID sourceId,
        UUID targetId,
        Double value
) {

}
