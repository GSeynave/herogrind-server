package com.shock.herogrind.world.internal.application;

import java.time.Instant;
import java.util.Collections;

import org.springframework.stereotype.Component;

import com.shock.herogrind.party.api.PartyFacade;
import com.shock.herogrind.world.internal.domain.HeroActivity;
import com.shock.herogrind.world.internal.domain.HeroActivityEvent;
import com.shock.herogrind.world.internal.domain.HeroActivityState;
import com.shock.herogrind.world.internal.domain.WorldEvent;
import com.shock.herogrind.world.internal.domain.WorldEventType;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class IdleActivityHandler implements ActivityHandler {

  private final PartyFacade partyFacade;

  @Override
  public ActivityHandlerResult handle(HeroActivity currentActivity) {
    // Handle the idle activity logic here
    // For example, you might want to log that the hero is idle or perform some
    // other action
    System.out.println("Hero is idle: " + currentActivity.heroId());
    var party = partyFacade.getPartyInfoByHeroId(currentActivity.heroId());
    if (party.isPresent()) {
      var now = Instant.now().toEpochMilli();
      System.out.println("Hero is in party: " + party.get().partyId());
      var nextActivity = new HeroActivity(
          currentActivity.heroId(),
          party.get().areaId(),
          HeroActivityState.ROAMING,
          now,
          now + 1000);
      var event = new WorldEvent(
          WorldEventType.HERO_STARTED_ROAMING,
          new HeroActivityEvent(
              currentActivity.heroId(),
              currentActivity.areaId()),
          now);
      return new ActivityHandlerResult(nextActivity, Collections.singletonList(event));
    }

    System.out.println("Hero is not in a party.");

    return new ActivityHandlerResult(null, Collections.emptyList());
  }

  @Override
  public boolean supports(HeroActivityState state) {
    return state == HeroActivityState.IDLE;
  }
}
