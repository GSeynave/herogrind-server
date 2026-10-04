package com.shock.herogrind.world.internal.application;

import org.springframework.stereotype.Component;

import com.shock.herogrind.combat.api.CombatFacade;
import com.shock.herogrind.world.internal.application.HeroActivityView.EncounterPayloadView;
import com.shock.herogrind.world.internal.application.HeroActivityView.GhostPayloadView;
import com.shock.herogrind.world.internal.domain.GhostActivityRepository;
import com.shock.herogrind.world.internal.domain.HeroActivity;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class HeroActivityViewAssembler {

  private final CombatFacade combatFacade;
  private final GhostActivityRepository ghostActivityRepository;

  public HeroActivityView assemble(HeroActivity activity) {
    return switch (activity.state()) {
      case IN_ENCOUNTER -> assembleFromEncounter(activity);
      case GHOST_TRAVELING, GHOST_RESURRECTING -> assembleFromGhost(activity);
      default -> assembleDefaultView(activity);
    };
  }

  public HeroActivityView assembleFromEncounter(HeroActivity activity) {
    var encounter = combatFacade.getEncounterByHeroId(activity.heroId())
        .orElseThrow(() -> new IllegalStateException("No encounter found for hero " + activity.heroId()));
    return new HeroActivityView(
        activity.heroId(),
        activity.areaId(),
        activity.state(),
        activity.startedAt(),
        new EncounterPayloadView(encounter.encounterId(), encounter.enemyId()));
  }

  public HeroActivityView assembleFromGhost(HeroActivity activity) {
    var ghost = ghostActivityRepository.findByHeroId(activity.heroId())
        .orElseThrow(() -> new IllegalStateException("No ghost info found for hero " + activity.heroId()));
    return new HeroActivityView(
        activity.heroId(),
        activity.areaId(),
        activity.state(),
        activity.startedAt(),
        new GhostPayloadView(
            ghost.getCurrentAreaId(),
            ghost.getTravelDestinationId(),
            ghost.getArrivalAt(),
            ghost.getResurrectionEndAt()));
  }

  public HeroActivityView assembleDefaultView(HeroActivity activity) {
    return new HeroActivityView(
        activity.heroId(),
        activity.areaId(),
        activity.state(),
        activity.startedAt(),
        null);
  }
}
