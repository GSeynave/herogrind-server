package com.shock.herogrind.combat.api;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CombatFacade {

  EncounterInfo startEncounter(UUID heroId, UUID areaId);

  EncounterInfo getEncounterById(UUID encounterId);

  Optional<EncounterInfo> getEncounterByHeroId(UUID heroId);

  EncounterInfo advanceEncounter(UUID encounterId);

  void endEncounter(UUID encounterId);

  List<EncounterInfo> findAll();

}
