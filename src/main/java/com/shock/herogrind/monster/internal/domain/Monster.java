
package com.shock.herogrind.monster.internal.domain;

import java.util.UUID;

public record Monster(
    UUID id,
    String name,
    Double health,
    Double attackDamage,
    UUID areaId) {

}
