package com.shock.herogrind.world.internal.domain;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class WorldPositionTest {

  @Test
  void acceptsNormalizedCoordinatesAtTheAreaBoundaries() {
    assertDoesNotThrow(() -> new WorldPosition(0, 1));
  }

  @Test
  void rejectsCoordinatesOutsideTheNormalizedArea() {
    assertThrows(IllegalArgumentException.class, () -> new WorldPosition(-0.01, 0.5));
    assertThrows(IllegalArgumentException.class, () -> new WorldPosition(0.5, 1.01));
  }
}
