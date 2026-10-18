package com.shock.herogrind.world.api.web.dto;

import java.util.List;

public record WorldStateDto(

    List<HeroActivityDto> heroActivities,
    List<EncounterDto> encounters,
    List<GhostDto> ghosts) {

}
