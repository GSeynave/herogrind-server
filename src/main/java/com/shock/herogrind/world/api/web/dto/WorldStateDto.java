package com.shock.herogrind.world.api.web.dto;

import java.util.List;

import com.shock.herogrind.world.internal.application.WorldStateView;

public record WorldStateDto(

    List<HeroActivityDto> heroActivities,
    List<EncounterDto> encounters,
    List<GhostDto> ghosts,
    List<WorldActorDto> actors) {

  public static WorldStateDto from(WorldStateView view) {
    return new WorldStateDto(
        view.heroActivities().stream().map(HeroActivityDto::from).toList(),
        view.encounters().stream().map(EncounterDto::from).toList(),
        view.ghosts().stream().map(GhostDto::from).toList(),
        view.actors().stream().map(WorldActorDto::from).toList());
  }

}
