package com.shock.herogrind.party.api;

import java.util.List;
import java.util.UUID;

public interface PartyFacade {

  List<PartyInfo> getPartyInfo();

  void removeHeroFromParty(UUID heroId, UUID areaId);
}
