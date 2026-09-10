package com.shock.herogrind.world.internal.application;

import java.util.List;

import com.shock.herogrind.combat.api.CombatActionInfo;
import com.shock.herogrind.world.internal.domain.HeroActivity;

public record EncounterActivityResult(
    HeroActivity activity,
    List<CombatActionInfo> combatActions) {
}
