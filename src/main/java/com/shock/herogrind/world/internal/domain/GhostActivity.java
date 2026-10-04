package com.shock.herogrind.world.internal.domain;

import java.util.List;
import java.util.UUID;

import com.shock.herogrind.combat.api.GhostInfo;

import lombok.Data;

@Data
public class GhostActivity implements HeroActivityPayload {

  private List<UUID> pathToTown;
  private UUID currentAreaId;
  private UUID travelDestinationId;
  private Long arrivalAt;
  private Long resurrectionEndAt;
  private HeroActivityState state;
  private UUID heroId;

  public static GhostActivity fromGhostInfo(GhostInfo ghostInfo) {
    GhostActivity activity = new GhostActivity();
    activity.setPathToTown(ghostInfo.pathToTown());
    activity.setCurrentAreaId(ghostInfo.currentAreaId());
    activity.setTravelDestinationId(ghostInfo.travelDestinationId());
    activity.setArrivalAt(ghostInfo.arrivalAt());
    activity.setResurrectionEndAt(ghostInfo.resurrectionEndAt());
    activity.setState(ghostInfo.state());
    activity.setHeroId(ghostInfo.heroId());
    return activity;

  }
}
