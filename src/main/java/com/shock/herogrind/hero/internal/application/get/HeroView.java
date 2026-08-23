package com.shock.herogrind.hero.internal.application.get;

import com.shock.herogrind.hero.internal.domain.Hero;
import com.shock.herogrind.hero.internal.domain.HeroRole;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class HeroView {

    private UUID id;
    private String name;
    private HeroRole role;
    private Integer level;
    private Double health;
    private Double attack;
    private Double defense;

    public static HeroView from(Hero domain) {

        return new HeroView(
                domain.getId(), domain.getName(), domain.getRole(), domain.getLevel(), domain.getHealth(),
                domain.getAttack(), domain.getDefense());
    }
}
