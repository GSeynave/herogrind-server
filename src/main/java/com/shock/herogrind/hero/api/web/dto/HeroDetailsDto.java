package com.shock.herogrind.hero.api.web.dto;

import com.shock.herogrind.hero.internal.application.get.HeroDetailsView;

import java.util.UUID;

public record HeroDetailsDto(
        UUID id,
        String name,
        String role,
        Integer level,
        Double health,
        Double attack,
        Double defense) {

    public static HeroDetailsDto from(HeroDetailsView view) {

        return new HeroDetailsDto(
                view.getId(), view.getName(), view.getRole().name(), view.getLevel(), view.getHealth(),
                view.getAttackDamage(), view.getDefense());
    }
}
