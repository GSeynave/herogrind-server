package com.shock.herogrind.hero.api;

import com.shock.herogrind.hero.internal.application.get.HeroDetailsView;
import lombok.Builder;

import java.util.UUID;

@Builder
public record HeroInfo(
        UUID id,
        String name,
        Double health,
        Double attackDamage
) {
    public static HeroInfo from(HeroDetailsView view) {
        return new HeroInfo(view.getId(), view.getName(), view.getHealth(), view.getAttackDamage());
    }
}
