package com.shock.herogrind.world.internal.application;

import java.util.List;

import org.springframework.stereotype.Component;

import com.shock.herogrind.world.internal.domain.WorldActor;
import com.shock.herogrind.world.internal.domain.WorldActorBehavior;

@Component
public class AttackingActorBehaviorHandler implements ActorBehaviorHandler {

  @Override
  public boolean supports(WorldActorBehavior behavior) {
    return behavior == WorldActorBehavior.ATTACKING;
  }

  @Override
  public WorldActor handle(WorldActor actor, List<WorldActor> actorSnapshot) {
    return actorSnapshot.stream()
        .filter(candidate -> candidate.id().equals(actor.targetId()))
        .filter(actor::canPursue)
        .findFirst()
        .map(target -> actor.canAttack(target) ? actor : actor.pursue(target.id()))
        .orElseGet(actor::resumeRoaming);
  }
}
