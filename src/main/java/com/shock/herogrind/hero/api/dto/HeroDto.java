package com.shock.herogrind.hero.api.dto;

import com.shock.herogrind.hero.internal.application.get.HeroView;
import com.shock.herogrind.hero.internal.domain.HeroRole;

import java.util.UUID;

public record HeroDto(
        UUID id,
        String name,
        HeroRole role,
        Integer level,
        Double health,
        Double attack,
        Double defense) {

    public static HeroDto from(HeroView view) {

        return new HeroDto(
                view.getId(), view.getName(), view.getRole(), view.getLevel(), view.getHealth(),
                view.getAttack(), view.getDefense());
    }
}
