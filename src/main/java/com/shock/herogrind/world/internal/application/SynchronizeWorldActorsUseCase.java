package com.shock.herogrind.world.internal.application;

import java.util.UUID;

import org.springframework.stereotype.Component;

import com.shock.herogrind.hero.api.HeroFacade;
import com.shock.herogrind.world.internal.domain.ActorType;
import com.shock.herogrind.world.internal.domain.HeroActivity;
import com.shock.herogrind.world.internal.domain.HeroActivityRepository;
import com.shock.herogrind.world.internal.domain.HeroActivityState;
import com.shock.herogrind.world.internal.domain.WorldActor;
import com.shock.herogrind.world.internal.domain.WorldActorBehavior;
import com.shock.herogrind.world.internal.domain.WorldActorRepository;
import com.shock.herogrind.world.internal.domain.WorldPosition;

import lombok.RequiredArgsConstructor;

/**
 * Keeps every hero's persistent world actor aligned with its current activity.
 *
 * <p>
 * Existing actor state is never recreated here. Activity changes update only
 * its area and
 * behavior, preserving movement, health and targeting state once simulation
 * starts managing it.
 * </p>
 */
@Component
@RequiredArgsConstructor
public class SynchronizeWorldActorsUseCase {

  private static final WorldPosition INITIAL_HERO_POSITION = new WorldPosition(0.5, 0.5);

  private final HeroActivityRepository heroActivityRepository;
  private final HeroFacade heroFacade;
  private final WorldActorRepository worldActorRepository;

  public void synchronizeHeroes() {
    heroFacade.getAllHeroes().forEach(hero -> {
      var activity = heroActivityRepository.getOrIdle(hero.getId());
      worldActorRepository.findById(hero.getId())
          .ifPresentOrElse(
              actor -> synchronizeExistingActor(actor, activity),
              () -> createHeroActor(activity.heroId(), activity.areaId(), behaviorFor(activity.state())));
    });
  }

  private void synchronizeExistingActor(WorldActor actor, HeroActivity activity) {
    if (activity.state() == HeroActivityState.IDLE
        && actor.behavior() == WorldActorBehavior.GHOST_RESURRECTING) {
      worldActorRepository.save(actor.resurrect(activity.areaId()));
      return;
    }
    if (isAutonomousActivity(activity.state()) && isInCombatBehavior(actor.behavior())) {
      return;
    }
    worldActorRepository.save(actor.withActivity(activity.areaId(), behaviorFor(activity.state())));
  }

  private boolean isAutonomousActivity(HeroActivityState activityState) {
    return activityState == HeroActivityState.IDLE || activityState == HeroActivityState.ROAMING;
  }

  private boolean isInCombatBehavior(WorldActorBehavior behavior) {
    return behavior == WorldActorBehavior.PURSUING || behavior == WorldActorBehavior.ATTACKING;
  }

  private WorldActor createHeroActor(UUID heroId, UUID areaId, WorldActorBehavior behavior) {
    var hero = heroFacade.getHeroInfoById(heroId);
    var actor = new WorldActor(
        hero.id(),
        ActorType.HERO,
        areaId,
        INITIAL_HERO_POSITION,
        INITIAL_HERO_POSITION,
        behavior,
        null,
        hero.health(),
        hero.health(),
        hero.attackDamage(),
        WorldActorTuning.HERO_ATTACK_RANGE,
        WorldActorTuning.HERO_AGGRO_RANGE,
        WorldActorTuning.HERO_MOVEMENT_SPEED_PER_TICK,
        0);
    worldActorRepository.save(actor);
    return actor;
  }

  private WorldActorBehavior behaviorFor(HeroActivityState activityState) {
    return switch (activityState) {
      case IDLE, HERO_RESURRECTED, ROAMING -> WorldActorBehavior.ROAMING;
      case IN_ENCOUNTER -> WorldActorBehavior.ATTACKING;
      case DUNGEON -> WorldActorBehavior.DUNGEON;
      case DYING -> WorldActorBehavior.DYING;
      case DEAD -> WorldActorBehavior.DEAD;
      case GHOST_WAITING -> WorldActorBehavior.GHOST_WAITING;
      case GHOST_TRAVELING -> WorldActorBehavior.GHOST_TRAVELING;
      case GHOST_RESURRECTING -> WorldActorBehavior.GHOST_RESURRECTING;
    };
  }
}
