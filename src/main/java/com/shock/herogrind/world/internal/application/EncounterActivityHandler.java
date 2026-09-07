package com.shock.herogrind.world.internal.application;

import com.shock.herogrind.combat.api.CombatFacade;
import com.shock.herogrind.combat.api.EncounterStatusInfo;
import com.shock.herogrind.world.internal.domain.HeroActivity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
@RequiredArgsConstructor
public class EncounterActivityHandler {

    private final CombatFacade combatFacade;

    public EncounterActivityResult handle(HeroActivity activity) {
        var encounterInfo = combatFacade.getEncounterById(activity.encounterId());
        if(encounterInfo.status().equals(EncounterStatusInfo.ENDED)) {
            combatFacade.endEncounter(encounterInfo.encounterId());
            return new EncounterActivityResult(
                    HeroActivity.roaming(activity.heroId(), activity.areaId()),
                    new ArrayList<>()
            );
        }
        if (!encounterInfo.isReadyForResolution()) {
            return new EncounterActivityResult(
                    HeroActivity.roaming(activity.heroId(), activity.areaId()),
                    new ArrayList<>()
            );
        }
        var result = combatFacade.advanceEncounter(activity.encounterId());
        var combatActions = result.actions();
        if (result.status().equals(EncounterStatusInfo.ENDED)) {
            if (result.heroHealth() <= 0) {
                combatFacade.endEncounter(result.encounterId());
                return new EncounterActivityResult(
                        HeroActivity.dead(activity.heroId(), activity.areaId()),
                        combatActions
                );
            }
            return new EncounterActivityResult(
            HeroActivity.roaming(activity.heroId(), activity.areaId()),
            combatActions
            );
        }
        return new EncounterActivityResult(
            activity,
            combatActions
        );
    }
}
