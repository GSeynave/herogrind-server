package com.shock.herogrind.hero.internal.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Hero {

    private UUID id;
    private String name;
    private HeroRole role;
    private Integer level;
    private Double health;
    private Double attack;
    private Double defense;

}
