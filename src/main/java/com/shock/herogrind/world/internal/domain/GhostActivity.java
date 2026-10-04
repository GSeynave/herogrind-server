package com.shock.herogrind.world.internal.domain;

import java.util.List;
import java.util.UUID;

import lombok.Data;

@Data
public class GhostActivity implements HeroActivityPayload {

  private List<UUID> pathToTown;
  private UUID currentAreaId;
  private UUID travelDestinationId;
  private Long arrivalAt;
  private Long resurrectionEndAt;
  private UUID heroId;
}
