package com.shock.herogrind.world.api.web.dto;

import com.shock.herogrind.world.internal.application.HeroActivityView;
import com.shock.herogrind.world.internal.domain.HeroActivityState;

import java.util.UUID;

public record HeroActivityDto(
        UUID heroId,
        UUID areaId,
        HeroActivityState state,
        UUID encounterId
) {

    public static HeroActivityDto from(HeroActivityView view) {
        return new HeroActivityDto(
                view.heroId(),
                view.areaId(),
                view.state(),
                view.encounterId()
        );
    }
}
