package com.shock.herogrind.world.internal.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class WorldSpatialRulesTest {

  @Test
  void determinesWhetherActorsAreWithinAttackRange() {
    var source = new WorldPosition(0.1, 0.1);
    var target = new WorldPosition(0.4, 0.1);

    assertTrue(WorldSpatialRules.isWithinRange(source, target, 0.3));
    assertFalse(WorldSpatialRules.isWithinRange(source, target, 0.29));
  }

  @Test
  void movesTowardsTargetWithoutOvershootingIt() {
    var source = new WorldPosition(0.1, 0.1);
    var target = new WorldPosition(0.9, 0.1);

    assertEquals(new WorldPosition(0.4, 0.1),
        WorldSpatialRules.moveTowards(source, target, 0.3));
    assertEquals(target, WorldSpatialRules.moveTowards(source, target, 1));
  }
}
