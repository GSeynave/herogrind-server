package com.shock.herogrind.world.internal.application;

import java.util.ArrayList;

import org.springframework.stereotype.Component;

import com.shock.herogrind.combat.api.CombatFacade;
import com.shock.herogrind.combat.api.GhostInfo;
import com.shock.herogrind.monster.api.MonsterFacade;
import com.shock.herogrind.world.api.web.dto.HeroActivityDto;
import com.shock.herogrind.world.api.web.dto.WorldStateDto;
import com.shock.herogrind.world.internal.domain.GhostActivityRepository;

@Component
public class GetWorldStateUseCase {

  private final GetHeroActivitiesUseCase getHeroActivitiesUseCase;
  private final CombatFacade combatFacade;
  private final GhostActivityRepository ghostActivityRepository;
  // private final MonsterFacade monsterFacade;

  public GetWorldStateUseCase(
      GetHeroActivitiesUseCase getHeroActivitiesUseCase,
      CombatFacade combatFacade,
      GhostActivityRepository ghostActivityRepository,
      MonsterFacade monsterFacade) {
    this.getHeroActivitiesUseCase = getHeroActivitiesUseCase;
    this.combatFacade = combatFacade;
    this.ghostActivityRepository = ghostActivityRepository;
    // this.monsterFacade = monsterFacade;
  }

  public WorldStateDto execute() {
    // FIXME : Create propper DTOs for encounters and ghosts, and map them here
    var heroActivities = getHeroActivitiesUseCase.execute();
    var heroes = heroActivities.stream()
        .map(HeroActivityDto::from)
        .toList();
    var encounters = combatFacade.findAll();
    var ghosts = ghostActivityRepository.findAll();
    var ghostInfos = new ArrayList<GhostInfo>();
    ghosts.forEach(ghost -> {
      ghostInfos.add(
          GhostInfo.builder()
              .heroId(ghost.getHeroId())
              .currentAreaId(ghost.getCurrentAreaId())
              .travelDestinationId(ghost.getTravelDestinationId())
              .arrivalAt(ghost.getArrivalAt())
              .resurrectionEndAt(ghost.getResurrectionEndAt())
              .build());
    });
    // var monsters = monsterFacade.findAll();

    return new WorldStateDto(
        heroes,
        encounters,
        ghostInfos);
  }
}
