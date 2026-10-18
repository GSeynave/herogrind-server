package com.shock.herogrind.world.internal.application;

import org.springframework.stereotype.Component;

import com.shock.herogrind.combat.api.CombatFacade;
import com.shock.herogrind.monster.api.MonsterFacade;
import com.shock.herogrind.world.api.web.dto.EncounterDto;
import com.shock.herogrind.world.api.web.dto.GhostDto;
import com.shock.herogrind.world.api.web.dto.HeroActivityDto;
import com.shock.herogrind.world.api.web.dto.WorldStateDto;
import com.shock.herogrind.world.internal.domain.GhostActivityRepository;

@Component
public class GetWorldStateUseCase {

  private final GetHeroActivitiesUseCase getHeroActivitiesUseCase;
  private final CombatFacade combatFacade;
  private final GhostActivityRepository ghostActivityRepository;

  public GetWorldStateUseCase(
      GetHeroActivitiesUseCase getHeroActivitiesUseCase,
      CombatFacade combatFacade,
      GhostActivityRepository ghostActivityRepository,
      MonsterFacade monsterFacade) {
    this.getHeroActivitiesUseCase = getHeroActivitiesUseCase;
    this.combatFacade = combatFacade;
    this.ghostActivityRepository = ghostActivityRepository;
  }

  public WorldStateDto execute() {
    var heroActivities = getHeroActivitiesUseCase.execute();
    var heroes = heroActivities.stream()
        .map(HeroActivityDto::from)
        .toList();
    var encounters = combatFacade.findAll();
    var encounterDtos = encounters.stream()
        .map(encounter -> new EncounterDto(
            encounter.encounterId(),
            encounter.heroId(),
            encounter.heroHealth(),
            encounter.enemyId(),
            encounter.enemyHealth()))
        .toList();
    var ghosts = ghostActivityRepository.findAll();
    var ghostDtos = ghosts.stream()
        .map(ghost -> new GhostDto(
            ghost.getHeroId(),
            ghost.getCurrentAreaId(),
            ghost.getTravelDestinationId(),
            ghost.getStartedAt(),
            ghost.getArrivalAt(),
            ghost.getResurrectionEndAt()))
        .toList();

    return new WorldStateDto(
        heroes,
        encounterDtos,
        ghostDtos);
  }
}
