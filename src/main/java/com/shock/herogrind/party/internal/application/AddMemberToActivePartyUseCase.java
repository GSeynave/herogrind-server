package com.shock.herogrind.party.internal.application;

import com.shock.herogrind.hero.api.HeroFacade;
import com.shock.herogrind.party.internal.application.get.PartyView;
import com.shock.herogrind.party.internal.application.get.PartyViewMapper;
import com.shock.herogrind.party.internal.domain.Party;
import com.shock.herogrind.party.internal.domain.PartyRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AddMemberToActivePartyUseCase {

    private final PartyRepository partyRepository;
    private final HeroFacade heroFacade;
    private final PartyViewMapper partyViewMapper;


    public PartyView execute(AddMemberToActivePartyCommand command) {
        var hero = heroFacade.getHeroById(command.memberId());

        var partyOptional = partyRepository.findActive();

        var party = partyOptional.orElseGet(Party::active);
        party.addMember(hero.getId());
        partyRepository.save(party);

        return partyViewMapper.from(party);
    }
}
