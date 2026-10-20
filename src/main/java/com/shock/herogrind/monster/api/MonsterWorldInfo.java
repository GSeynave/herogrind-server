package com.shock.herogrind.monster.api;

import java.util.UUID;

/**
 * Monster information needed by the world simulation, including its assigned area.
 */
public record MonsterWorldInfo(
    UUID id,
    String name,
    Double health,
    Double attackDamage,
    UUID areaId) {
}
