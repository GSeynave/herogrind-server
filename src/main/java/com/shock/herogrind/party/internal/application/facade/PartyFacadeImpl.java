package com.shock.herogrind.party.internal.application.facade;

import com.shock.herogrind.hero.api.HeroPartyInfo;
import com.shock.herogrind.party.api.PartyFacade;
import com.shock.herogrind.party.api.PartyInfo;
import com.shock.herogrind.party.internal.application.get.GetPartiesUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class PartyFacadeImpl implements PartyFacade {

    private final GetPartiesUseCase getPartiesUseCase;

    @Override
    public List<PartyInfo> getPartyInfo() {
        var parties = getPartiesUseCase.execute();


        return parties.stream()
                .map(p ->
                        new PartyInfo(
                                p.id(),
                                p.members().stream().map(HeroPartyInfo::getId).toList(),
                                p.areaId(),
                                p.partyType()))
                .toList();
    }
}
