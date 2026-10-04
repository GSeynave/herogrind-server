package com.shock.herogrind.world.internal.application;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Component;

import com.shock.herogrind.area.api.AreaFacade;
import com.shock.herogrind.combat.api.GhostInfo;
import com.shock.herogrind.world.internal.domain.GhostActivity;
import com.shock.herogrind.world.internal.domain.HeroActivity;
import com.shock.herogrind.world.internal.domain.HeroActivityState;

import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class GhostActivityHandler {

  private static final int DEFAULT_TRAVEL_TIME = 10;
  private static final int DEFAULT_RESURRECTION_TIME = 10;
  private final AreaFacade areaFacade;

  public Optional<HeroActivity> handle(HeroActivity activity) {
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

  Optional<HeroActivity> startTravel(HeroActivity activity) {
    if (activity.state().equals(HeroActivityState.GHOST_WAITING)) {
      List<UUID> pathToTown = new ArrayList<>();
      pathToTown.add(activity.areaId()); // fake path to town, just add the current area id for now
      var townId = areaFacade.getAreas().stream()
          .filter(a -> a.isTown())
          .findFirst();
      pathToTown.add(townId.get().id());
      var graveyardId = areaFacade.getAreas().stream()
          .filter(a -> a.isGraveyard())
          .findFirst();
      var ghostInfo = new GhostInfo(pathToTown, graveyardId.get().id(), townId.get().id(),
          Instant.now().plus(DEFAULT_TRAVEL_TIME, ChronoUnit.SECONDS).toEpochMilli(), null,
          HeroActivityState.GHOST_TRAVELING,
          activity.heroId());
      return Optional
          .of(activity.ghostTraveling(activity.heroId(), activity.areaId(), GhostActivity.fromGhostInfo(ghostInfo)));
    }
    return Optional.empty();
    // if activity state = GHOST_WAITING, then we need to resolve the path to the
    // town.
    // get areas.
    // algorithm to resolve path using AreaConnection[].
    // calculate time from graveyard to town using ghost speed and distance.
    // switch activity state to GHOST_TRAVELING and set the time to travel.
  }

  Optional<HeroActivity> resolveTravel(HeroActivity activity) {
    if (activity.state().equals(HeroActivityState.GHOST_TRAVELING)) {
      var payload = activity.payload();
      if (payload == null) {
        throw new IllegalStateException("Ghost info is null for GHOST_TRAVELING activity");
      }
      GhostActivity ghostInfo = (GhostActivity) payload.get();
      if (Instant.now().toEpochMilli() < ghostInfo.getArrivalAt()) {
        return Optional.empty(); // unchanged
      } else {
        // if current area is not town,
        if (!areaFacade.getAreaById(ghostInfo.getTravelDestinationId()).isTown()) {
          // travel to the next destination from the pathToTown list
        }

        else {
          // if current area is town, then switch to GHOST_RESURRECTING and set the time
          // to wait
          var resurrectionEndAt = Instant.now().plus(DEFAULT_RESURRECTION_TIME, ChronoUnit.SECONDS).toEpochMilli();
          var ghostInfoUpdated = new GhostInfo(
              new ArrayList<>(),
              ghostInfo.getTravelDestinationId(),
              null,
              null,
              resurrectionEndAt,
              HeroActivityState.GHOST_RESURRECTING,
              activity.heroId());
          return Optional.of(
              HeroActivity.ghostResurrecting(
                  activity.heroId(),
                  activity.areaId(),
                  GhostActivity.fromGhostInfo(ghostInfoUpdated)));
        }
      }
    }
    return Optional.empty();
    // if now < arrivalAt : unchanged
    // else reutrn GHOST_RESURRECTING with resuctionEndAt
  }

  Optional<HeroActivity> resolveResurrection(HeroActivity activity) {
    if (activity.state().equals(HeroActivityState.GHOST_RESURRECTING)) {
      var payload = activity.payload();
      if (payload == null) {
        throw new IllegalStateException("Ghost info is null for GHOST_RESURRECTING activity");
      }
      GhostActivity ghostInfo = (GhostActivity) payload.get();
      if (Instant.now().toEpochMilli() < ghostInfo.getResurrectionEndAt()) {
        return Optional.empty(); // unchanged
      } else {
        // if now >= resurrectionEndAt, then switch to IDLE in town
        return Optional.of(HeroActivity.idle(activity.heroId()));
      }
    }
    return Optional.empty();
    // if now < resurrectionEndAt : unchanged
    // else return IDLE in town
  }

}
