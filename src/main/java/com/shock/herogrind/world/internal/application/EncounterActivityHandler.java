package com.shock.herogrind.world.internal.application;

import java.util.ArrayList;

import org.springframework.stereotype.Component;

import com.shock.herogrind.combat.api.CombatFacade;
import com.shock.herogrind.combat.api.EncounterStatusInfo;
import com.shock.herogrind.world.internal.domain.HeroActivity;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class EncounterActivityHandler {

  private final CombatFacade combatFacade;

  public EncounterActivityResult handle(HeroActivity activity) {
    if (activity.encounterInfo().isEmpty()) {
      throw new IllegalStateException("Encounter info is missing for activity: " + activity);
    }
    var encounter = activity.encounterInfo().get();
    var encounterInfo = combatFacade.getEncounterById(encounter.encounterId());
    if (encounterInfo.status().equals(EncounterStatusInfo.ENDED)) {
      combatFacade.endEncounter(encounterInfo.encounterId());
      return new EncounterActivityResult(
          HeroActivity.roaming(activity.heroId(), activity.areaId()),
          new ArrayList<>());
    }
    if (!encounterInfo.isReadyForResolution()) {
      return new EncounterActivityResult(
          activity,
          new ArrayList<>());
    }
    var result = combatFacade.advanceEncounter(encounterInfo.encounterId());
    var combatActions = result.actions();
    if (result.status().equals(EncounterStatusInfo.ENDED)) {
      if (result.heroHealth() <= 0) {
        combatFacade.endEncounter(result.encounterId());
        return new EncounterActivityResult(
            HeroActivity.dying(activity.heroId(), activity.areaId()),
            combatActions);
      }
      return new EncounterActivityResult(
          HeroActivity.roaming(activity.heroId(), activity.areaId()),
          combatActions);
    }
    return new EncounterActivityResult(
        activity,
        combatActions);
  }
}
