package com.shock.herogrind.world.api.web.dto;

import java.util.UUID;

import com.shock.herogrind.world.internal.application.WorldStateView.EncounterView;

public record EncounterDto(
    UUID encounterId,
    UUID heroId,
    Double heroHealth,
    UUID enemyId,
    Double enemyHealth) {

  public static EncounterDto from(EncounterView view) {
    return new EncounterDto(
        view.encounterId(),
        view.heroId(),
        view.heroHealth(),
        view.enemyId(),
        view.enemyHealth());
  }
}
