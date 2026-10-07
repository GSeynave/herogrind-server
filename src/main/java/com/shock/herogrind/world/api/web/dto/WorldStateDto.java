package com.shock.herogrind.world.api.web.dto;

import java.util.List;

import com.shock.herogrind.combat.api.EncounterInfo;
import com.shock.herogrind.combat.api.GhostInfo;

public record WorldStateDto(

    List<HeroActivityDto> heroActivities,
    List<EncounterInfo> encounters,
    List<GhostInfo> ghosts) {

}
