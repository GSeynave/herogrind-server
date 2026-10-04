package com.shock.herogrind.world.internal.application;

import java.util.ArrayList;

import org.springframework.stereotype.Component;

import com.shock.herogrind.combat.api.CombatFacade;
import com.shock.herogrind.combat.api.EncounterStatusInfo;
import com.shock.herogrind.party.api.PartyFacade;
import com.shock.herogrind.world.internal.domain.EncounterActivity;
import com.shock.herogrind.world.internal.domain.HeroActivity;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class EncounterActivityHandler {

  private final CombatFacade combatFacade;
  private final PartyFacade partyFacade;

  public EncounterActivityResult handle(HeroActivity activity) {
    if (activity.payload().isEmpty()) {
      throw new IllegalStateException("Encounter info is missing for activity: " + activity);
    }
    EncounterActivity encounter = (EncounterActivity) activity.payload().get();
    encounter.log();
    var encounterInfo = combatFacade.getEncounterById(encounter.getEncounterId());
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
        partyFacade.removeHeroFromParty(activity.heroId(), activity.areaId());
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
