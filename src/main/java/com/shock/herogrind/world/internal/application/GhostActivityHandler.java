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
import com.shock.herogrind.world.internal.domain.HeroActivity;
import com.shock.herogrind.world.internal.domain.HeroActivityState;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class GhostActivityHandler {

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
      areaFacade.getAreas().stream()
          .filter(a -> a.isTown())
          .findFirst()
          .ifPresent(town -> pathToTown.add(town.id()));
      var ghostInfo = new GhostInfo(pathToTown, Instant.now().plus(10, ChronoUnit.SECONDS).toEpochMilli(), null);
      return Optional.of(activity.ghostTraveling(activity.heroId(), activity.areaId(), ghostInfo));
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
    // if now < arrivalAt : unchanged
    // else reutrn GHOST_RESURRECTING with resuctionEndAt
  }

  Optional<HeroActivity> resolveResurrection(HeroActivity activity) {
    // if now < resurrectionEndAt : unchanged
    // else return IDLE in town
  }

}
