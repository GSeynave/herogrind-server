package com.shock.herogrind.world.internal.domain;

public sealed interface WorldEventPayload permits HeroActivityEvent, CombatActionEvent{
}
