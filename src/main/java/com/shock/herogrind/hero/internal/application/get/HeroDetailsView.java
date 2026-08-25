package com.shock.herogrind.hero.internal.application.get;

import com.shock.herogrind.hero.internal.domain.Hero;
import com.shock.herogrind.hero.internal.domain.HeroRole;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class HeroDetailsView {

    private UUID id;
    private String name;
    private HeroRole role;
    private Integer level;
    private Double health;
    private Double attack;
    private Double defense;

    public static HeroDetailsView from(Hero domain) {

        return new HeroDetailsView(
                domain.getId(), domain.getName(), domain.getRole(), domain.getLevel(), domain.getHealth(),
                domain.getAttack(), domain.getDefense());
    }
}
