package com.shock.herogrind.world.internal.application;

import org.springframework.stereotype.Component;

import java.util.List;

import com.shock.herogrind.world.internal.domain.WorldActor;
import com.shock.herogrind.world.internal.domain.WorldActorBehavior;
import com.shock.herogrind.world.internal.domain.WorldSpatialRules;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class RoamingActorBehaviorHandler implements ActorBehaviorHandler {

  private final RandomWorldPositionSource randomWorldPositionSource;
  private final WorldTargetSelector worldTargetSelector;

  @Override
  public boolean supports(WorldActorBehavior behavior) {
    return behavior == WorldActorBehavior.ROAMING;
  }

  @Override
  public WorldActor handle(WorldActor actor, List<WorldActor> actorSnapshot) {
    var incomingAttacker = actorSnapshot.stream()
        .filter(actor::canCounterAttack)
        .findFirst();
    if (incomingAttacker.isPresent()) {
      return actor.attack(incomingAttacker.get().id());
    }
    var target = worldTargetSelector.select(actorSnapshot.stream()
        .filter(actor::canAggro)
        .toList());
    if (target.isPresent()) {
      return actor.pursue(target.get().id());
    }
    var destination = hasReachedDestination(actor)
        ? randomWorldPositionSource.nextAreaPosition()
        : actor.roamingDestination();
    var nextPosition = WorldSpatialRules.moveTowards(
        actor.position(),
        destination,
        actor.movementSpeed());
    return actor.withRoamingPosition(nextPosition, destination);
  }

  private boolean hasReachedDestination(WorldActor actor) {
    return WorldSpatialRules.distanceBetween(actor.position(), actor.roamingDestination()) == 0;
  }
}
