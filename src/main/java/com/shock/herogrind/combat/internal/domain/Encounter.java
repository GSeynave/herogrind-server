package com.shock.herogrind.combat.internal.domain;

import com.shock.herogrind.combat.api.EncounterInfo;
import com.shock.herogrind.combat.api.EncounterStatusInfo;
import com.shock.herogrind.hero.api.HeroInfo;
import com.shock.herogrind.monster.api.MonsterInfo;

import java.util.List;
import java.util.UUID;

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
    public static final double DEFAULT_HERO_ATTACK_SPEED = 1D;
    public static final double DEFAULT_MONSTER_ATTACK_SPEED = 1.5D;

    public static Encounter start(HeroInfo heroInfo, MonsterInfo monsterInfo, Long currentTime) {
        return new Encounter(
                UUID.randomUUID(),
                heroInfo.id(),
                heroInfo.health(),
                currentTime,
                getNextActionTime(DEFAULT_HERO_ATTACK_SPEED, currentTime),
                monsterInfo.id(),
                monsterInfo.health(),
                currentTime,
                getNextActionTime(DEFAULT_MONSTER_ATTACK_SPEED, currentTime),
                EncounterStatus.STARTING
        );
    }

    private static Long getNextActionTime(Double attackSpeed, Long lastActionAt) {
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
        var updatedEncounter = new Encounter(
                id,
                heroId,
                heroHealth,
                currentTime,
                getNextActionTime(DEFAULT_HERO_ATTACK_SPEED, currentTime),
                enemyId,
                newEnemyHealth,
                enemyLastActionAt,
                enemyNextActionAt,
                newEnemyHealth <= 0 ? EncounterStatus.ENDED : status
        );
        var action = new CombatAction(
                CombatActionType.ATTACK,
                heroId,
                enemyId,
                damage
        );
        return new CombatStepResult(updatedEncounter, action);
    }

    public CombatStepResult executeEnemyAction(double damage) {
        double newHeroHealth = Math.max(0, heroHealth - damage);
        long currentTime = System.currentTimeMillis();
        var updatedEncounter = new Encounter(
                id,
                heroId,
                newHeroHealth,
                heroLastActionAt,
                heroNextActionAt,
                enemyId,
                enemyHealth,
                currentTime,
                getNextActionTime(DEFAULT_MONSTER_ATTACK_SPEED, currentTime),
                newHeroHealth <= 0 ? EncounterStatus.ENDED : status
        );
        var action = new CombatAction(
                CombatActionType.ATTACK,
                enemyId,
                heroId,
                damage
        );
        return new CombatStepResult(updatedEncounter, action);
    }

    public Boolean isEnded() {
        return status == EncounterStatus.ENDED;
    }

    public static EncounterInfo toInfo(Encounter encounter, List<CombatAction> actionsList){
        return new EncounterInfo(
                encounter.id(),
                encounter.heroId(),
                encounter.heroHealth(),
                encounter.enemyId(),
                encounter.enemyHealth(),
                EncounterStatusInfo.valueOf(encounter.status().name()),
                actionsList.stream().map(CombatAction::toInfo).toList(),
                encounter.getNextResolutionTime()
        );
    }
}
