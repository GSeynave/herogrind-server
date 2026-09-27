package com.shock.herogrind.world.api.web.dto;

import java.util.UUID;

import com.shock.herogrind.world.internal.application.HeroActivityView;
import com.shock.herogrind.world.internal.application.HeroActivityView.EncounterPayloadView;
import com.shock.herogrind.world.internal.application.HeroActivityView.GhostPayloadView;
import com.shock.herogrind.world.internal.domain.HeroActivityState;

public record HeroActivityDto(
    UUID heroId,
    UUID areaId,
    HeroActivityState state,
    Long startedAt,
    ActivityPayloadDto payload) {

  public static HeroActivityDto from(HeroActivityView view) {
    return new HeroActivityDto(
        view.heroId(),
        view.areaId(),
        view.state(),
        view.startedAt(),
        ActivityPayloadDto.from(view.payload()));
  }

  public sealed interface ActivityPayloadDto
      permits EncounterPayloadDto, GhostPayloadDto {

    static ActivityPayloadDto from(
        HeroActivityView.ActivityPayloadView payload) {

      if (payload == null) {
        return null;
      }

      return switch (payload) {
        case EncounterPayloadView encounter ->
          new EncounterPayloadDto(encounter.encounterId());

        case GhostPayloadView ghost ->
          new GhostPayloadDto(
              ghost.currentAreaId(),
              ghost.travelDestinationId(),
              ghost.arrivalAt(),
              ghost.resurrectionEndAt());
      };
    }
  }

  public record EncounterPayloadDto(
      UUID encounterId)
      implements ActivityPayloadDto {
  }

  public record GhostPayloadDto(
      UUID currentAreaId,
      UUID travelDestinationId,
      Long arrivalAt,
      Long resurrectionEndAt)
      implements ActivityPayloadDto {
  }
}
