package com.shock.herogrind.world.api.web.dto;

import java.util.UUID;

import com.shock.herogrind.world.internal.application.WorldStateView.ActorView;

public record WorldActorDto(
    UUID id,
    String type,
    UUID areaId,
    double positionX,
    double positionY,
    String behavior,
    UUID targetId,
    double health,
    double maxHealth) {

  public static WorldActorDto from(ActorView view) {
    return new WorldActorDto(
        view.id(),
        view.type().name(),
        view.areaId(),
        view.positionX(),
        view.positionY(),
        view.behavior().name(),
        view.targetId(),
        view.health(),
        view.maxHealth());
  }
}
