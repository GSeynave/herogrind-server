package com.shock.herogrind.hero.internal.application.get;

import com.shock.herogrind.hero.internal.domain.Hero;
import com.shock.herogrind.hero.internal.domain.HeroRole;

import java.util.UUID;

public class HeroFactory {

    public static Hero hero() {
        return Hero.builder()
                .id(UUID.randomUUID())
                .name("Random hero")
                .role(HeroRole.MELEE)
                .level(1)
                .health(20D)
                .defense(0D)
                .attack(2D)
                .build();
    }
}
