package com.shock.herogrind.world.internal.application;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Component;

import com.shock.herogrind.party.api.PartyFacade;
import com.shock.herogrind.world.internal.domain.ActorCombatAction;
import com.shock.herogrind.world.internal.domain.ActorType;
import com.shock.herogrind.world.internal.domain.HeroActivity;
import com.shock.herogrind.world.internal.domain.HeroActivityRepository;
import com.shock.herogrind.world.internal.domain.HeroActivityState;
import com.shock.herogrind.world.internal.domain.WorldActorBehavior;
import com.shock.herogrind.world.internal.domain.WorldActorRepository;
import com.shock.herogrind.world.internal.domain.WorldEvent;

import lombok.RequiredArgsConstructor;

/**
 * Moves defeated hero actors into the existing death and ghost activity
 * lifecycle.
 */
@Component
@RequiredArgsConstructor
public class HeroActorDeathTransitionUseCase {

  private final WorldActorRepository worldActorRepository;
  private final HeroActivityRepository heroActivityRepository;
  private final PartyFacade partyFacade;

  public List<WorldEvent> transitionDefeatedHeroes(List<ActorCombatAction> combatActions) {
    return combatActions.stream()
        .filter(action -> action.targetHealth() == 0)
        .map(ActorCombatAction::targetId)
        .distinct()
        .map(this::transitionHeroIfDefeated)
        .flatMap(java.util.Optional::stream)
        .toList();
  }

  private Optional<WorldEvent> transitionHeroIfDefeated(UUID actorId) {
    return worldActorRepository.findById(actorId)
        .filter(actor -> actor.type() == ActorType.HERO)
        .filter(actor -> actor.behavior() == WorldActorBehavior.DEAD)
        .map(actor -> {
          var currentActivity = heroActivityRepository.getOrIdle(actor.id());
          partyFacade.removeHeroFromParty(actor.id(), currentActivity.areaId());
          var now = Instant.now();
          var dyingActivity = new HeroActivity(
              actor.id(),
              currentActivity.areaId(),
              HeroActivityState.DYING,
              now.toEpochMilli(),
              now.plus(3, ChronoUnit.SECONDS).toEpochMilli());
          heroActivityRepository.save(dyingActivity);
          worldActorRepository.save(actor.withActivity(actor.areaId(), WorldActorBehavior.DYING));
          return WorldEvent.from(dyingActivity);
        });
  }
}
