package com.shock.herogrind.world.internal.application;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Component;

import com.shock.herogrind.combat.api.CombatFacade;
import com.shock.herogrind.world.internal.domain.EncounterEvent;
import com.shock.herogrind.world.internal.domain.HeroActivity;
import com.shock.herogrind.world.internal.domain.HeroActivityState;
import com.shock.herogrind.world.internal.domain.WorldEvent;
import com.shock.herogrind.world.internal.domain.WorldEventType;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class RoamingActivityHandler implements ActivityHandler {

  private final CombatFacade combatFacade;

  private static final int ENCOUNTER_CHANCE_PERCENT = 25;
  private static final int ROAMING_DURATION_SECONDS = 2;

  @Override
  public boolean supports(HeroActivityState state) {
    return state == HeroActivityState.ROAMING;
  }

  @Override
  public ActivityHandlerResult handle(HeroActivity currentActivity) {
    if (!currentActivity.isReadyForNextActivity()) {
      return ActivityHandlerResult.empty();
    }

    var now = Instant.now();

    var encounterTrigger = Math.random() * 100 < ENCOUNTER_CHANCE_PERCENT;
    if (!encounterTrigger) {
      var nextActivity = new HeroActivity(
          currentActivity.heroId(),
          currentActivity.areaId(),
          HeroActivityState.ROAMING,
          now.toEpochMilli(),
          now.plus(ROAMING_DURATION_SECONDS, ChronoUnit.SECONDS).toEpochMilli());

      return new ActivityHandlerResult(
          nextActivity,
          Collections.emptyList());
    }

    var encounter = combatFacade.startEncounter(currentActivity.heroId(), currentActivity.areaId());
    var nextActivity = new HeroActivity(
        currentActivity.heroId(),
        currentActivity.areaId(),
        HeroActivityState.IN_ENCOUNTER,
        now.toEpochMilli(),
        encounter.nextResolutionAt());

    var event = new WorldEvent(
        WorldEventType.HERO_STARTED_ENCOUNTER,
        new EncounterEvent(
            encounter.encounterId(),
            encounter.heroId(),
            encounter.heroHealth(),
            encounter.enemyId(),
            encounter.enemyHealth()),
        now.toEpochMilli());
    return new ActivityHandlerResult(
        nextActivity,
        List.of(event));
  }

}
