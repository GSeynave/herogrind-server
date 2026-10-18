package com.shock.herogrind.world.api.web.dto;

import java.util.UUID;

import com.shock.herogrind.world.internal.application.WorldStateView.GhostView;

public record GhostDto(
    UUID heroId,
    UUID currentAreaId,
    UUID travelDestinationId,
    Long startedAt,
    Long arrivalAt,
    Long resurrectionEndAt) {

  public static GhostDto from(GhostView view) {
    return new GhostDto(
        view.heroId(),
        view.currentAreaId(),
        view.travelDestinationId(),
        view.startedAt(),
        view.arrivalAt(),
        view.resurrectionEndAt());
  }
}
