package com.shock.herogrind.world.internal.domain;

import java.util.UUID;

public record WorldEvent(
        WorldEventType type,
        UUID heroId,
        UUID areaId,
        Long occurredAt
        ) {
    public static WorldEvent from(HeroActivity activity) {
        return new WorldEvent(getType(activity.state()), activity.heroId(), activity.areaId(), System.currentTimeMillis());
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
