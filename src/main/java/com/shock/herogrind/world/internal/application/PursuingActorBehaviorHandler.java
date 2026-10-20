package com.shock.herogrind.world.internal.application;

import java.util.List;

import org.springframework.stereotype.Component;

import com.shock.herogrind.world.internal.domain.WorldActor;
import com.shock.herogrind.world.internal.domain.WorldActorBehavior;
import com.shock.herogrind.world.internal.domain.WorldSpatialRules;

@Component
public class PursuingActorBehaviorHandler implements ActorBehaviorHandler {

  @Override
  public boolean supports(WorldActorBehavior behavior) {
    return behavior == WorldActorBehavior.PURSUING;
  }

  @Override
  public WorldActor handle(WorldActor actor, List<WorldActor> actorSnapshot) {
    return actorSnapshot.stream()
        .filter(candidate -> candidate.id().equals(actor.targetId()))
        .filter(actor::canPursue)
        .findFirst()
        .map(target -> pursueTarget(actor, target))
        .orElseGet(actor::resumeRoaming);
  }

  private WorldActor pursueTarget(WorldActor actor, WorldActor target) {
    if (actor.canAttack(target)) {
      return actor.attackTarget();
    }
    var nextPosition = WorldSpatialRules.moveTowards(
        actor.position(),
        target.position(),
        actor.movementSpeed());
    return actor.withPursuitPosition(nextPosition);
  }
}
