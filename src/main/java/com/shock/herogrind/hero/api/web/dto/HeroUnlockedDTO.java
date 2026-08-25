package com.shock.herogrind.hero.api.web.dto;

import java.util.UUID;

public record HeroUnlockedDTO(
        UUID id
) {

    public static HeroUnlockedDTO from(UUID uuid) {

        return new HeroUnlockedDTO(uuid);
    }
}
