package com.shock.herogrind.combat.internal.domain;

import java.util.List;
import java.util.UUID;

import com.shock.herogrind.combat.api.EncounterInfo;
import com.shock.herogrind.combat.api.EncounterStatusInfo;
import com.shock.herogrind.hero.api.HeroInfo;
import com.shock.herogrind.monster.api.MonsterInfo;

public record Encounter(
    UUID id,

    UUID heroId,
    double heroHealth,
    long heroLastActionAt,
    long heroNextActionAt,

    UUID enemyId,
    double enemyHealth,
    long enemyLastActionAt,
    long enemyNextActionAt,

    EncounterStatus status

) {
  public static final double DEFAULT_HERO_ATTACK_SPEED = 0.5D;
  public static final double DEFAULT_MONSTER_ATTACK_SPEED = 0.33D;

  public static Encounter start(HeroInfo heroInfo, MonsterInfo monsterInfo, Long currentTime) {
    return new Encounter(
        UUID.randomUUID(),
        heroInfo.id(),
        heroInfo.health(),
        currentTime,
        getAttackInterval(DEFAULT_HERO_ATTACK_SPEED, currentTime),
        monsterInfo.id(),
        monsterInfo.health(),
        currentTime,
        getAttackInterval(DEFAULT_MONSTER_ATTACK_SPEED, currentTime),
        EncounterStatus.STARTING);
  }

  private static Long getAttackInterval(Double attackSpeed, Long lastActionAt) {
    return lastActionAt + (long) (1000 / attackSpeed);
  }

  public Long getNextResolutionTime() {
    return Math.min(heroNextActionAt, enemyNextActionAt);
  }

  public boolean isHeroReadyToAct() {
    return heroNextActionAt <= System.currentTimeMillis();
  }

  public boolean isEnemyReadyToAct() {
    return enemyNextActionAt <= System.currentTimeMillis();
  }

  public CombatStepResult executeHeroAction(double damage) {
    double newEnemyHealth = Math.max(0, enemyHealth - damage);
    long currentTime = System.currentTimeMillis();
    System.err.println("Executing hero action: " + damage + " damage to enemy. New enemy health: " + newEnemyHealth);
    var updatedEncounter = new Encounter(
        id,
        heroId,
        heroHealth,
        currentTime,
        getAttackInterval(DEFAULT_HERO_ATTACK_SPEED, currentTime),
        enemyId,
        newEnemyHealth,
        enemyLastActionAt,
        enemyNextActionAt,
        newEnemyHealth <= 0 ? EncounterStatus.ENDED : status);
    var action = new CombatAction(
        CombatActionType.ATTACK,
        heroId,
        enemyId,
        damage,
        newEnemyHealth);
    return new CombatStepResult(updatedEncounter, action);
  }

  public CombatStepResult executeEnemyAction(double damage) {
    double newHeroHealth = Math.max(0, heroHealth - damage);
    long currentTime = System.currentTimeMillis();
    System.err.println("Executing enemy action: " + damage + " damage to hero. New hero health: " + newHeroHealth);
    System.err.println("New Health: " + newHeroHealth);
    var newState = newHeroHealth <= 0 ? EncounterStatus.ENDED : status;
    System.err.println("New status: " + newState);
    var updatedEncounter = new Encounter(
        id,
        heroId,
        newHeroHealth,
        heroLastActionAt,
        heroNextActionAt,
        enemyId,
        enemyHealth,
        currentTime,
        getAttackInterval(DEFAULT_MONSTER_ATTACK_SPEED, currentTime),
        newState);
    var action = new CombatAction(
        CombatActionType.ATTACK,
        enemyId,
        heroId,
        damage,
        newHeroHealth);
    return new CombatStepResult(updatedEncounter, action);
  }

  public Boolean isEnded() {
    return status == EncounterStatus.ENDED;
  }

  public static EncounterInfo toInfo(Encounter encounter, List<CombatAction> actionsList) {
    return new EncounterInfo(
        encounter.id(),
        encounter.heroId(),
        encounter.heroHealth(),
        encounter.enemyId(),
        encounter.enemyHealth(),
        EncounterStatusInfo.valueOf(encounter.status().name()),
        actionsList.stream().map(CombatAction::toInfo).toList(),
        encounter.getNextResolutionTime());
  }
}
