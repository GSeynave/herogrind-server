package com.shock.herogrind.world.internal.application;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;

import org.springframework.stereotype.Component;

import com.shock.herogrind.area.api.AreaFacade;
import com.shock.herogrind.area.api.AreaInfo;
import com.shock.herogrind.world.internal.domain.GhostActivity;
import com.shock.herogrind.world.internal.domain.GhostActivityRepository;
import com.shock.herogrind.world.internal.domain.HeroActivity;
import com.shock.herogrind.world.internal.domain.HeroActivityState;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DeathHandler implements ActivityHandler {

  private final AreaFacade areaFacade;
  private final GhostActivityRepository ghostActivityRepository;

  @Override
  public boolean supports(HeroActivityState state) {
    return state.equals(HeroActivityState.DYING) || state.equals(HeroActivityState.DEAD);
  }

  @Override
  public ActivityHandlerResult handle(HeroActivity currentActivity) {
    if (!currentActivity.isReadyForNextActivity()) {
      return ActivityHandlerResult.empty();
    }

    return switch (currentActivity.state()) {
      case DYING -> handleDying(currentActivity);
      case DEAD -> handleDead(currentActivity);
      default -> throw new IllegalStateException(
          "Invalid state for DeathHandler: " + currentActivity.state());
    };
  }

  private ActivityHandlerResult handleDead(HeroActivity currentActivity) {
    var now = Instant.now();

    var graveyard = areaFacade.getAreas().stream()
        .filter(AreaInfo::isGraveyard)
        .findFirst()
        .orElseThrow(() -> new IllegalStateException("No graveyard found"));

    var ghostActivity = new GhostActivity();
    ghostActivity.setHeroId(currentActivity.heroId());
    ghostActivity.setCurrentAreaId(graveyard.id());
    ghostActivity.setPathToTown(new ArrayList<>());

    ghostActivityRepository.save(ghostActivity);

    var nextActivity = new HeroActivity(
        currentActivity.heroId(),
        graveyard.id(),
        HeroActivityState.GHOST_WAITING,
        now.toEpochMilli(),
        now.toEpochMilli());

    return new ActivityHandlerResult(
        nextActivity,
        Collections.emptyList());
  }

  private ActivityHandlerResult handleDying(HeroActivity currentActivity) {
    var now = Instant.now();
    var nextActivity = new HeroActivity(
        currentActivity.heroId(),
        currentActivity.areaId(),
        HeroActivityState.DEAD,
        now.toEpochMilli(),
        now.plus(3, ChronoUnit.SECONDS).toEpochMilli());
    return new ActivityHandlerResult(
        nextActivity,
        Collections.emptyList());
  }

}
