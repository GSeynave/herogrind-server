package com.shock.herogrind.world.internal.domain;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

public record HeroActivity(
        UUID heroId,
        UUID areaId,
        HeroActivityState state,
        UUID encounterId,
        Long startedAt,
        Long nextResolutionAt
) {


    public static HeroActivity idle(UUID heroId){
        var now = Instant.now();
        return new HeroActivity(heroId,
                null,
                HeroActivityState.IDLE,
                null,
                now.toEpochMilli(),
                now.plus(0, ChronoUnit.SECONDS).toEpochMilli()
        );
    }
    public static HeroActivity inDungeon(UUID heroId){
        var now = Instant.now();
        return new HeroActivity(heroId,
                null,
                HeroActivityState.DUNGEON,
                null,
                now.toEpochMilli(),
                now.plus(3, ChronoUnit.SECONDS).toEpochMilli()
        );
    }
    public static HeroActivity inEncounter(UUID heroId, UUID areaId){
        var now = Instant.now();
        return new HeroActivity(heroId,
                areaId,
                HeroActivityState.IN_ENCOUNTER,
                UUID.randomUUID(),
                now.toEpochMilli(),
                now.plus(3, ChronoUnit.SECONDS).toEpochMilli()
        );
    }
    public static HeroActivity roaming(UUID heroId, UUID areaId){
        var now = Instant.now();
        return new HeroActivity(heroId,
                areaId,
                HeroActivityState.ROAMING,
                null,
                now.toEpochMilli(),
                now.plus(2, ChronoUnit.SECONDS).toEpochMilli()
        );
    }

    public Boolean isReadyForNextActivity(){
        return Instant.now().toEpochMilli()  >= this.nextResolutionAt;
    }

    public String log(){
        return String.format("Hero {%s} is currently in {%s}", heroId, state());
    }
}
