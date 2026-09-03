package com.shock.herogrind.combat.internal.application.facade;

import com.shock.herogrind.combat.api.CombatFacade;
import com.shock.herogrind.combat.api.EncounterInfo;
import com.shock.herogrind.combat.internal.application.EncounterRepository;
import com.shock.herogrind.combat.internal.domain.CombatAction;
import com.shock.herogrind.combat.internal.domain.Encounter;
import com.shock.herogrind.hero.api.HeroFacade;
import com.shock.herogrind.monster.api.MonsterFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class CombatFacadeImpl implements CombatFacade {

    private final MonsterFacade monsterFacade;
    private final HeroFacade heroFacade;
    private final EncounterRepository encounterRepository;

    @Override
    public EncounterInfo getEncounterById(UUID encounterId) {
        return Encounter.toInfo(encounterRepository.findById(encounterId), new ArrayList<>());
    }

    @Override
    public EncounterInfo startEncounter(UUID heroId, UUID areaId) {
        var enemy = monsterFacade.getMonsterInfoByAreaId(areaId);
        var hero = heroFacade.getHeroInfoById(heroId);
        var now = System.currentTimeMillis();
        var encounter = Encounter.start(hero, enemy, now);
        encounterRepository.save(encounter);

        return Encounter.toInfo(encounter, new ArrayList<>());
    }

    @Override
    public EncounterInfo advanceEncounter(UUID encounterId) {
        var encounter = encounterRepository.findById(encounterId);
        var actionsList = new ArrayList<CombatAction>();

        if (!encounter.isEnded() && encounter.isHeroReadyToAct()){
            var hero = heroFacade.getHeroInfoById(encounter.heroId());
            var step =encounter.executeHeroAction(hero.attackDamage());
            encounter = step.encounter();
            actionsList.add(step.action());
        }
        if (!encounter.isEnded() && encounter.isEnemyReadyToAct()){
            var monster = monsterFacade.getMonsterInfoById(encounter.enemyId());
            var step = encounter.executeEnemyAction(monster.attackDamage());
            encounter = step.encounter();
            actionsList.add(step.action());
        }
        return Encounter.toInfo(encounter, actionsList);
    }
}
