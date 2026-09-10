package com.shock.herogrind.hero.api.web.dto;

import com.shock.herogrind.hero.internal.application.get.HeroDetailsView;
import com.shock.herogrind.hero.internal.domain.HeroRole;

import java.util.UUID;

public record HeroDetailsDto(
        UUID id,
        String name,
        HeroRole role,
        Integer level,
        Double health,
        Double attack,
        Double defense) {

    public static HeroDetailsDto from(HeroDetailsView view) {

        return new HeroDetailsDto(
                view.getId(), view.getName(), view.getRole(), view.getLevel(), view.getHealth(),
                view.getAttackDamage(), view.getDefense());
    }
}
