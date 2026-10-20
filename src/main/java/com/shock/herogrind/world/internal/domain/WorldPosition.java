package com.shock.herogrind.world.internal.domain;

/**
 * A normalized position within an area's walkable space.
 *
 * <p>Coordinates are deliberately independent from client canvas pixels so the server remains
 * authoritative over world movement.</p>
 */
public record WorldPosition(double x, double y) {

  public WorldPosition {
    validateCoordinate(x, "x");
    validateCoordinate(y, "y");
  }

  private static void validateCoordinate(double coordinate, String axis) {
    if (!Double.isFinite(coordinate) || coordinate < 0 || coordinate > 1) {
      throw new IllegalArgumentException(axis + " must be a finite value between 0 and 1");
    }
  }
}
