package com.shock.herogrind.party.internal.application.facade;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Component;

import com.shock.herogrind.hero.api.HeroPartyInfo;
import com.shock.herogrind.party.api.PartyFacade;
import com.shock.herogrind.party.api.PartyInfo;
import com.shock.herogrind.party.internal.application.RemoveMemberFromAreaPartyCommand;
import com.shock.herogrind.party.internal.application.RemoveMemberFromAreaPartyUseCase;
import com.shock.herogrind.party.internal.application.get.GetPartiesUseCase;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class PartyFacadeImpl implements PartyFacade {

  private final GetPartiesUseCase getPartiesUseCase;
  private final RemoveMemberFromAreaPartyUseCase removeMemberFromAreaPartyUseCase;

  @Override
  public List<PartyInfo> getPartyInfo() {
    var parties = getPartiesUseCase.execute();

    return parties.stream()
        .map(p -> new PartyInfo(
            p.id(),
            p.members().stream().map(HeroPartyInfo::getId).toList(),
            p.areaId(),
            p.partyType()))
        .toList();
  }

  @Override
  public void removeHeroFromParty(UUID heroId, UUID areaId) {
    RemoveMemberFromAreaPartyCommand command = new RemoveMemberFromAreaPartyCommand(heroId, areaId);
    removeMemberFromAreaPartyUseCase.execute(command);

  }

  @Override
  public Optional<PartyInfo> getPartyInfoByHeroId(UUID heroId) {
    var parties = getPartiesUseCase.execute();
    return parties.stream()
        .filter(p -> p.members().stream().anyMatch(m -> m.getId().equals(heroId)))
        .findFirst()
        .map(p -> new PartyInfo(
            p.id(),
            p.members().stream().map(HeroPartyInfo::getId).toList(),
            p.areaId(),
            p.partyType()));
  }

}
