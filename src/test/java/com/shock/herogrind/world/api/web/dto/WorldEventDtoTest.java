package com.shock.herogrind.world.api.web.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

import java.util.UUID;

import org.junit.jupiter.api.Test;

import com.shock.herogrind.combat.api.CombatActionTypeInfo;
import com.shock.herogrind.world.internal.domain.CombatActionEvent;
import com.shock.herogrind.world.internal.domain.WorldEvent;
import com.shock.herogrind.world.internal.domain.WorldEventType;

class WorldEventDtoTest {

  @Test
  void preservesTheCombatEventContract() {
    var sourceId = UUID.randomUUID();
    var targetId = UUID.randomUUID();
    var event = new WorldEvent(
        WorldEventType.COMBAT_ACTION,
        new CombatActionEvent(sourceId, targetId, 12D, CombatActionTypeInfo.ATTACK, 3D),
        123L);

    var response = WorldEventDto.from(event);

    assertEquals("COMBAT_ACTION", response.eventType());
    assertEquals(123L, response.occurredAt());
    var payload = assertInstanceOf(CombatActionEventDto.class, response.payload());
    assertEquals(sourceId, payload.sourceId());
    assertEquals(targetId, payload.targetId());
    assertEquals(12D, payload.targetHealth());
    assertEquals("ATTACK", payload.actionType());
    assertEquals(3D, payload.value());

  }
}
