package com.shock.herogrind.world.internal.application;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.shock.herogrind.world.internal.domain.ActorCombatAction;
import com.shock.herogrind.world.internal.domain.ActorType;
import com.shock.herogrind.world.internal.domain.WorldActor;
import com.shock.herogrind.world.internal.domain.WorldActorBehavior;
import com.shock.herogrind.world.internal.domain.WorldActorRepository;

import lombok.RequiredArgsConstructor;

/**
 * Resolves every attack from one actor-tick snapshot before persisting resulting health.
 */
@Component
@RequiredArgsConstructor
public class CombatResolutionUseCase {

  private final WorldActorRepository worldActorRepository;

  public List<ActorCombatAction> resolve(List<WorldActor> actors, long now) {
    var actorsById = actors.stream().collect(Collectors.toMap(WorldActor::id, Function.identity()));
    var attackSources = actors.stream()
        .filter(actor -> actor.behavior() == WorldActorBehavior.ATTACKING)
        .filter(actor -> actor.nextAttackAt() <= now)
        .filter(actor -> actorsById.containsKey(actor.targetId()))
        .filter(actor -> actor.canAttack(actorsById.get(actor.targetId())))
        .toList();

    var damageByTarget = attackSources.stream()
        .collect(Collectors.groupingBy(
            WorldActor::targetId,
            Collectors.summingDouble(WorldActor::attackDamage)));

    var resolvedActors = actorsById.entrySet().stream()
        .collect(Collectors.toMap(
            Map.Entry::getKey,
            entry -> resolveActor(entry.getValue(), damageByTarget, attackSources, now)));
    resolvedActors.values().forEach(worldActorRepository::save);

    return attackSources.stream()
        .map(source -> new ActorCombatAction(
            source.id(),
            source.targetId(),
            source.attackDamage(),
            resolvedActors.get(source.targetId()).health()))
        .toList();
  }

  private WorldActor resolveActor(
      WorldActor actor,
      Map<java.util.UUID, Double> damageByTarget,
      List<WorldActor> attackSources,
      long now) {
    var resolvedActor = attackSources.stream().anyMatch(source -> source.id().equals(actor.id()))
        ? actor.withNextAttackAt(now + attackCooldownFor(actor.type()))
        : actor;
    var receivedDamage = damageByTarget.get(actor.id());
    return receivedDamage == null ? resolvedActor : resolvedActor.receiveDamage(receivedDamage);
  }

  private long attackCooldownFor(ActorType actorType) {
    return actorType == ActorType.HERO
        ? WorldActorTuning.HERO_ATTACK_COOLDOWN_MILLIS
        : WorldActorTuning.MONSTER_ATTACK_COOLDOWN_MILLIS;
  }
}
