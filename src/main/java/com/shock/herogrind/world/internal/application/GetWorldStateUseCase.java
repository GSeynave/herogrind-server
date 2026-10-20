package com.shock.herogrind.world.internal.application;

import org.springframework.stereotype.Component;

import com.shock.herogrind.combat.api.CombatFacade;
import com.shock.herogrind.world.internal.domain.GhostActivityRepository;
import com.shock.herogrind.world.internal.domain.WorldActorRepository;

@Component
public class GetWorldStateUseCase {

  private final GetHeroActivitiesUseCase getHeroActivitiesUseCase;
  private final CombatFacade combatFacade;
  private final GhostActivityRepository ghostActivityRepository;
  private final WorldActorRepository worldActorRepository;

  public GetWorldStateUseCase(
      GetHeroActivitiesUseCase getHeroActivitiesUseCase,
      CombatFacade combatFacade,
      GhostActivityRepository ghostActivityRepository,
      WorldActorRepository worldActorRepository) {
    this.getHeroActivitiesUseCase = getHeroActivitiesUseCase;
    this.combatFacade = combatFacade;
    this.ghostActivityRepository = ghostActivityRepository;
    this.worldActorRepository = worldActorRepository;
  }

  public WorldStateView execute() {
    var heroActivities = getHeroActivitiesUseCase.execute();
    var encounters = combatFacade.findAll();
    var encounterViews = encounters.stream()
        .map(encounter -> new WorldStateView.EncounterView(
            encounter.encounterId(),
            encounter.heroId(),
            encounter.heroHealth(),
            encounter.enemyId(),
            encounter.enemyHealth()))
        .toList();
    var ghosts = ghostActivityRepository.findAll();
    var ghostViews = ghosts.stream()
        .map(ghost -> new WorldStateView.GhostView(
            ghost.getHeroId(),
            ghost.getCurrentAreaId(),
            ghost.getTravelDestinationId(),
            ghost.getStartedAt(),
            ghost.getArrivalAt(),
            ghost.getResurrectionEndAt()))
        .toList();
    var actorViews = worldActorRepository.findAll().stream()
        .map(actor -> new WorldStateView.ActorView(
            actor.id(),
            actor.type(),
            actor.areaId(),
            actor.position().x(),
            actor.position().y(),
            actor.behavior(),
            actor.targetId(),
            actor.health(),
            actor.maxHealth()))
        .toList();

    return new WorldStateView(
        heroActivities,
        encounterViews,
        ghostViews,
        actorViews);
  }
}
