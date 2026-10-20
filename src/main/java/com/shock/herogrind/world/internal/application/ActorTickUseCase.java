package com.shock.herogrind.world.internal.application;

import java.util.List;

import org.springframework.stereotype.Component;

import com.shock.herogrind.world.internal.domain.WorldActorRepository;
import com.shock.herogrind.world.internal.domain.ActorCombatAction;

import lombok.RequiredArgsConstructor;

/**
 * Advances all actors through the behavior handler that owns their current state.
 */
@Component
@RequiredArgsConstructor
public class ActorTickUseCase {

  private final WorldActorRepository worldActorRepository;
  private final List<ActorBehaviorHandler> behaviorHandlers;
  private final CombatResolutionUseCase combatResolutionUseCase;

  public List<ActorCombatAction> execute() {
    var actorSnapshot = worldActorRepository.findAll();
    var updatedActors = actorSnapshot.stream().map(actor -> behaviorHandlers.stream()
        .filter(handler -> handler.supports(actor.behavior()))
        .findFirst()
        .map(handler -> handler.handle(actor, actorSnapshot))
        .orElse(actor))
        .toList();
    updatedActors.forEach(worldActorRepository::save);
    return combatResolutionUseCase.resolve(updatedActors, System.currentTimeMillis());
  }
}
