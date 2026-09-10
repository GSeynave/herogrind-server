package com.shock.herogrind.world.internal.domain;

import java.util.UUID;

public record HeroActivityEvent(
        UUID heroId,
        UUID areaId
        ) implements WorldEventPayload {
}
