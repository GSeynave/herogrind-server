package com.shock.herogrind.world.internal.domain;

import com.shock.herogrind.combat.api.CombatActionInfo;

public record WorldEvent(
        WorldEventType eventType,
        WorldEventPayload payload,
        Long occurredAt
) {
    public static WorldEvent from(HeroActivity activity) {
        return new WorldEvent(getType(activity.state()), new HeroActivityEvent(activity.heroId(), activity.areaId()), System.currentTimeMillis());
    }

    public static WorldEvent from(CombatActionInfo action) {
        return new WorldEvent(WorldEventType.COMBAT_ACTION, new CombatActionEvent(action.sourceId(), action.targetId(), action.targetHealth(), action.type(), action.value()), System.currentTimeMillis());
    }

    private static WorldEventType getType(HeroActivityState state) {
        return switch (state) {
            case IDLE -> WorldEventType.HERO_IDLE;
            case DUNGEON -> WorldEventType.HERO_ENTERED_DUNGEON;
            case ROAMING -> WorldEventType.HERO_STARTED_ROAMING;
            case IN_ENCOUNTER -> WorldEventType.HERO_STARTED_ENCOUNTER;
            default -> throw new IllegalArgumentException("Unknown state: " + state);
        };
    }
}
