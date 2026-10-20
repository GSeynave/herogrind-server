package com.shock.herogrind.world.internal.domain;

/**
 * Pure spatial calculations used by the server-side world simulation.
 */
public final class WorldSpatialRules {

  private static final double RANGE_COMPARISON_TOLERANCE = 1e-9;

  private WorldSpatialRules() {
  }

  public static double distanceBetween(WorldPosition first, WorldPosition second) {
    return Math.hypot(second.x() - first.x(), second.y() - first.y());
  }

  public static boolean isWithinRange(
      WorldPosition source,
      WorldPosition target,
      double range) {
    if (!Double.isFinite(range) || range < 0) {
      throw new IllegalArgumentException("range must be a non-negative finite value");
    }
    return distanceBetween(source, target) <= range + RANGE_COMPARISON_TOLERANCE;
  }

  public static WorldPosition moveTowards(
      WorldPosition source,
      WorldPosition target,
      double maximumDistance) {
    if (!Double.isFinite(maximumDistance) || maximumDistance < 0) {
      throw new IllegalArgumentException("maximumDistance must be a non-negative finite value");
    }
    var distance = distanceBetween(source, target);
    if (distance == 0 || maximumDistance >= distance) {
      return target;
    }
    var progress = maximumDistance / distance;
    return new WorldPosition(
        source.x() + (target.x() - source.x()) * progress,
        source.y() + (target.y() - source.y()) * progress);
  }
}
