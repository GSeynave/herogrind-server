package com.shock.herogrind.world.api.web.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import com.shock.herogrind.world.internal.application.HeroActivityView;
import com.shock.herogrind.world.internal.application.WorldStateView;
import com.shock.herogrind.world.internal.domain.ActorType;
import com.shock.herogrind.world.internal.domain.HeroActivityState;
import com.shock.herogrind.world.internal.domain.WorldActorBehavior;

class WorldStateDtoTest {

  @Test
  void mapsApplicationViewsToTheExistingSnapshotContract() {
    var heroId = UUID.randomUUID();
    var areaId = UUID.randomUUID();
    var enemyId = UUID.randomUUID();
    var view = new WorldStateView(
        List.of(new HeroActivityView(heroId, areaId, HeroActivityState.IDLE, 10L, null)),
        List.of(new WorldStateView.EncounterView(UUID.randomUUID(), heroId, 50D, enemyId, 30D)),
        List.of(new WorldStateView.GhostView(heroId, areaId, null, 20L, null, null)),
        List.of(new WorldStateView.ActorView(
            enemyId,
            ActorType.MONSTER,
            areaId,
            .25,
            .75,
            WorldActorBehavior.ROAMING,
            null,
            30D,
            30D)));

    var response = WorldStateDto.from(view);

    assertEquals(heroId, response.heroActivities().getFirst().heroId());
    assertEquals(areaId, response.heroActivities().getFirst().areaId());
    assertEquals(enemyId, response.encounters().getFirst().enemyId());
    assertEquals(areaId, response.ghosts().getFirst().currentAreaId());
    assertEquals(enemyId, response.actors().getFirst().id());
    assertEquals("MONSTER", response.actors().getFirst().type());
    assertEquals(.25, response.actors().getFirst().positionX());
  }
}
