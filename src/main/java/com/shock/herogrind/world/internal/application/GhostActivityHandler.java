package com.shock.herogrind.world.internal.application;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Component;

import com.shock.herogrind.area.api.AreaFacade;
import com.shock.herogrind.area.api.AreaInfo;
import com.shock.herogrind.world.internal.domain.GhostActivity;
import com.shock.herogrind.world.internal.domain.GhostActivityRepository;
import com.shock.herogrind.world.internal.domain.GhostEvent;
import com.shock.herogrind.world.internal.domain.GhostState;
import com.shock.herogrind.world.internal.domain.HeroActivity;
import com.shock.herogrind.world.internal.domain.HeroActivityState;
import com.shock.herogrind.world.internal.domain.WorldEvent;
import com.shock.herogrind.world.internal.domain.WorldEventType;

import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class GhostActivityHandler implements ActivityHandler {

  private static final int DEFAULT_TRAVEL_TIME = 10;
  private static final int DEFAULT_RESURRECTION_TIME = 10;
  private final AreaFacade areaFacade;

  private final GhostActivityRepository ghostActivityRepository;

  @Override
  public boolean supports(HeroActivityState state) {
    return state.equals(HeroActivityState.GHOST_WAITING) ||
        state.equals(HeroActivityState.GHOST_TRAVELING) ||
        state.equals(HeroActivityState.GHOST_RESURRECTING);
  }

  @Override
  public ActivityHandlerResult handle(HeroActivity activity) {
    return switch (activity.state()) {
      case GHOST_WAITING -> startTravel(activity);
      case GHOST_TRAVELING -> resolveTravel(activity);
      case GHOST_RESURRECTING -> resolveResurrection(activity);
      default -> throw new IllegalStateException("Invalid activity state for ghost: " + activity.state());

      // if activity state = GHOST_TRAVELING, then we need to check if the hero has
      // arrived at the town.
      // if the hero has arrived, then we need to switch the activity state to
      // GHOST_RESURRECTING and set the time to wait.
      //
      // if activity state = GHOST_RESURRECTING, then we need to check if the hero has
      // waited long enough.
      // if the hero has waited long enough, then we need to switch the activity state
      // to IDLE and set the time to wait.
    };
  }

  ActivityHandlerResult startTravel(HeroActivity activity) {
    if (!activity.isReadyForNextActivity()) {
      return ActivityHandlerResult.empty(); // unchanged
    }
    var ghostInfo = ghostActivityRepository.findByHeroId(activity.heroId())
        .orElseThrow(
            () -> new IllegalStateException("Ghost info not found for hero: " + activity.heroId()));

    List<UUID> pathToTown = new ArrayList<>();
    var town = getTown();
    var graveyard = getGraveyard();
    pathToTown.add(graveyard.get().id());
    pathToTown.add(town.get().id());

    var arrivalAt = Instant.now().plus(DEFAULT_TRAVEL_TIME, ChronoUnit.SECONDS).toEpochMilli();
    var nextActivity = new HeroActivity(
        activity.heroId(),
        graveyard.get().id(),
        HeroActivityState.GHOST_TRAVELING,
        Instant.now().toEpochMilli(),
        arrivalAt);
    ghostInfo.setArrivalAt(arrivalAt);
    ghostInfo.setTravelDestinationId(town.get().id());
    ghostInfo.setPathToTown(pathToTown);
    ghostActivityRepository.save(ghostInfo);

    var event = new WorldEvent(WorldEventType.HERO_GHOST_TRAVELING,
        new GhostEvent(ghostInfo.getHeroId(),
            ghostInfo.getCurrentAreaId(),
            GhostState.TRAVELING,
            graveyard.get().id(),
            ghostInfo.getTravelDestinationId(),
            ghostInfo.getArrivalAt(),
            ghostInfo.getResurrectionEndAt()),
        System.currentTimeMillis());
    return new ActivityHandlerResult(nextActivity, List.of(event));
    // if activity state = GHOST_WAITING, then we need to resolve the path to the
    // town.
    // get areas.
    // algorithm to resolve path using AreaConnection[].
    // calculate time from graveyard to town using ghost speed and distance.
    // switch activity state to GHOST_TRAVELING and set the time to travel.
  }

  private Optional<AreaInfo> getGraveyard() {
    var graveyard = areaFacade.getAreas().stream()
        .filter(a -> a.isGraveyard())
        .findFirst();
    return graveyard;
  }

  private Optional<AreaInfo> getTown() {
    var town = areaFacade.getAreas().stream()
        .filter(a -> a.isTown())
        .findFirst();
    return town;
  }

  ActivityHandlerResult resolveTravel(HeroActivity activity) {
    var ghostInfo = ghostActivityRepository.findByHeroId(activity.heroId())
        .orElseThrow(
            () -> new IllegalStateException("Ghost info not found for hero: " + activity.heroId()));

    if (Instant.now().toEpochMilli() < ghostInfo.getArrivalAt()) {
      return ActivityHandlerResult.empty(); // unchanged
    }
    // if current area is not town,
    /*
     * FIXME : When i add the pathToTown list, we can use it to travel through
     * multiple areas to reach the town.
     * if (!areaFacade.getAreaById(ghostInfo.getTravelDestinationId()).isTown()) {
     * // travel to the next destination from the pathToTown list
     * // For now it's not possible as i force the next destionation to be town, but
     * in
     * // the future we can have multiple areas to travel through
     * }
     */

    return endGhostTravel(activity, ghostInfo);
  }

  private ActivityHandlerResult endGhostTravel(HeroActivity activity, GhostActivity ghostInfo) {
    // if current area is town, then switch to GHOST_RESURRECTING and set the time
    // to wait
    var resurrectionEndAt = Instant.now().plus(DEFAULT_RESURRECTION_TIME, ChronoUnit.SECONDS).toEpochMilli();
    var town = getTown();

    ghostInfo.setPathToTown(new ArrayList<>());
    ghostInfo.setCurrentAreaId(ghostInfo.getTravelDestinationId());
    ghostInfo.setTravelDestinationId(null);
    ghostInfo.setArrivalAt(null);
    ghostInfo.setResurrectionEndAt(resurrectionEndAt);
    ghostActivityRepository.save(ghostInfo);

    var nextActivity = new HeroActivity(activity.heroId(),
        town.get().id(),
        HeroActivityState.GHOST_RESURRECTING,
        Instant.now().toEpochMilli(),
        resurrectionEndAt);
    var event = new WorldEvent(WorldEventType.HERO_GHOST_RESURRECTING,
        new GhostEvent(ghostInfo.getHeroId(),
            ghostInfo.getCurrentAreaId(),
            GhostState.RESURRECTING,
            town.get().id(),
            null,
            ghostInfo.getArrivalAt(),
            ghostInfo.getResurrectionEndAt()),
        System.currentTimeMillis());
    return new ActivityHandlerResult(nextActivity, List.of(event));
  }

  ActivityHandlerResult resolveResurrection(HeroActivity activity) {
    var ghostInfo = ghostActivityRepository.findByHeroId(activity.heroId())
        .orElseThrow(
            () -> new IllegalStateException("Ghost info not found for hero: " + activity.heroId()));

    if (Instant.now().toEpochMilli() <= ghostInfo.getResurrectionEndAt()) {
      return ActivityHandlerResult.empty(); // unchanged
    }

    // if now >= resurrectionEndAt, then switch to IDLE in town
    ghostActivityRepository.deleteByHeroId(ghostInfo.getHeroId());
    var nextActivity = new HeroActivity(activity.heroId(),
        ghostInfo.getCurrentAreaId(),
        HeroActivityState.IDLE,
        Instant.now().toEpochMilli(),
        Instant.now().plus(0, ChronoUnit.SECONDS).toEpochMilli());
    var eventResurrected = new WorldEvent(
        WorldEventType.HERO_RESURRECTED,
        new GhostEvent(
            ghostInfo.getHeroId(),
            ghostInfo.getCurrentAreaId(),
            GhostState.RESURRECTING,
            ghostInfo.getCurrentAreaId(),
            ghostInfo.getTravelDestinationId(),
            ghostInfo.getArrivalAt(),
            ghostInfo.getResurrectionEndAt()),
        System.currentTimeMillis());
    return new ActivityHandlerResult(nextActivity, List.of(eventResurrected));
  }
}
