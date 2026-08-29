package com.shock.herogrind.world.internal.application;

import com.shock.herogrind.world.internal.domain.HeroActivity;
import com.shock.herogrind.world.internal.domain.HeroActivityState;

import java.util.UUID;

public record HeroActivityView(
        UUID heroId,
        UUID areaId,
        HeroActivityState state,
        UUID encounterId
) {

    public static HeroActivityView from(HeroActivity activity) {
        return new HeroActivityView(
                activity.heroId(),
                activity.areaId(),
                activity.state(),
                activity.encounterId()
        );
    }
}
