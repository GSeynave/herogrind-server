package com.shock.herogrind.monster.api;

import java.util.UUID;

public record MonsterInfo(
        UUID id,
        String name,
        Double health,
        Double attackDamage
) {

}
