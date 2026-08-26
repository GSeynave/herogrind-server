package com.shock.herogrind.world.internal.domain;

import java.util.UUID;

public record HeroActivity(
        UUID heroId,
        UUID areaId,
        HeroActivityState state,
        UUID encounterId
) {


    public static HeroActivity idle(UUID heroId){
        return new HeroActivity(heroId,
                null,
                HeroActivityState.IDLE,
                null);
    }
    public static HeroActivity inDungeon(UUID heroId){
        return new HeroActivity(heroId,
                null,
                HeroActivityState.DUNGEON,
                null);
    }
    public static HeroActivity roaming(UUID heroId, UUID areaId){
        return new HeroActivity(heroId,
                areaId,
                HeroActivityState.DUNGEON,
                null);
    }


    public String log(){
        return String.format("Hero {%s} is currently in {%s}", heroId, state());
    }
}
