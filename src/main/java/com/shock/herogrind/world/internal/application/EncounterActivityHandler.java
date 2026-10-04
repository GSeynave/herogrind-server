package com.shock.herogrind.world.internal.application;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

import org.springframework.stereotype.Component;

import com.shock.herogrind.combat.api.CombatFacade;
import com.shock.herogrind.combat.api.EncounterInfo;
import com.shock.herogrind.combat.api.EncounterStatusInfo;
import com.shock.herogrind.party.api.PartyFacade;
import com.shock.herogrind.world.internal.domain.CombatActionEvent;
import com.shock.herogrind.world.internal.domain.EncounterEvent;
import com.shock.herogrind.world.internal.domain.HeroActivity;
import com.shock.herogrind.world.internal.domain.HeroActivityState;
import com.shock.herogrind.world.internal.domain.WorldEvent;
import com.shock.herogrind.world.internal.domain.WorldEventType;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class EncounterActivityHandler implements ActivityHandler {

  private final CombatFacade combatFacade;
  private final PartyFacade partyFacade;

  @Override
  public boolean supports(HeroActivityState state) {
    return HeroActivityState.IN_ENCOUNTER.equals(state);
  }

  @Override
  public ActivityHandlerResult handle(HeroActivity activity) {
    EncounterInfo encounterInfo = combatFacade.getEncounterByHeroId(activity.heroId())
        .orElseThrow(() -> new IllegalStateException("No encounter found for hero " + activity.heroId()));

    var worldEvents = new ArrayList<WorldEvent>();

    if (!activity.isReadyForNextActivity()) {
      return ActivityHandlerResult.empty();
    }
    HeroActivity nextActivity = encounterInfo.status() == EncounterStatusInfo.ENDED
        ? finishEncounter(activity, encounterInfo, worldEvents)
        : handleOngoingEncounter(activity, encounterInfo, worldEvents);

    return new ActivityHandlerResult(nextActivity, worldEvents);
  }

  private HeroActivity handleOngoingEncounter(HeroActivity activity, EncounterInfo encounterInfo,
      ArrayList<WorldEvent> worldEvents) {
    var result = combatFacade.advanceEncounter(encounterInfo.encounterId());

    result.actions()
        .forEach(a -> worldEvents.add(
            new WorldEvent(WorldEventType.COMBAT_ACTION,
                new CombatActionEvent(
                    a.sourceId(),
                    a.targetId(),
                    a.targetHealth(),
                    a.type(),
                    a.value()),
                System.currentTimeMillis())));

    if (result.status() != (EncounterStatusInfo.ENDED)) {
      return new HeroActivity(
          activity.heroId(),
          activity.areaId(),
          HeroActivityState.IN_ENCOUNTER,
          activity.startedAt(),
          result.nextResolutionAt());
    }

    return finishEncounter(activity, result, worldEvents);

  }

  private HeroActivity finishEncounter(HeroActivity activity, EncounterInfo encounterInfo,
      ArrayList<WorldEvent> worldEvents) {
    combatFacade.endEncounter(encounterInfo.encounterId());
    addEncounterEndedEvent(worldEvents, encounterInfo);

    var now = Instant.now();
    if (encounterInfo.heroHealth() <= 0) {
      partyFacade.removeHeroFromParty(activity.heroId(), activity.areaId());

      return new HeroActivity(
          activity.heroId(),
          activity.areaId(),
          HeroActivityState.DYING,
          now.toEpochMilli(),
          now.plus(3, ChronoUnit.SECONDS).toEpochMilli());
    }
    return new HeroActivity(
        activity.heroId(),
        activity.areaId(),
        HeroActivityState.ROAMING,
        now.toEpochMilli(),
        now.plus(2, ChronoUnit.SECONDS).toEpochMilli());
  }

  private void addEncounterEndedEvent(ArrayList<WorldEvent> worldEvents, EncounterInfo result) {
    worldEvents.add(new WorldEvent(WorldEventType.HERO_ENDED_ENCOUNTER,
        new EncounterEvent(
            result.encounterId(),
            result.heroId(),
            result.heroHealth(),
            result.enemyId(),
            result.enemyHealth()),
        System.currentTimeMillis()));
  }
}
