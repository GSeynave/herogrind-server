package com.shock.herogrind.world.internal.application;

import com.shock.herogrind.combat.api.CombatActionInfo;
import com.shock.herogrind.world.internal.domain.HeroActivity;

import java.util.List;

public record EncounterActivityResult(
        HeroActivity activity,
        List<CombatActionInfo> combatActions
) {
}
