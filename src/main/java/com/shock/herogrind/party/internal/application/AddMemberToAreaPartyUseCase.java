package com.shock.herogrind.party.internal.application;

import com.shock.herogrind.area.api.AreaFacade;
import com.shock.herogrind.hero.api.HeroFacade;
import com.shock.herogrind.party.api.exception.AreaLockedException;
import com.shock.herogrind.party.internal.application.get.PartyView;
import com.shock.herogrind.party.internal.application.get.PartyViewMapper;
import com.shock.herogrind.party.internal.domain.Party;
import com.shock.herogrind.party.internal.domain.PartyRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AddMemberToAreaPartyUseCase {

    private final PartyRepository partyRepository;
    private final HeroFacade heroFacade;
    private final AreaFacade areaFacade;
    private final PartyViewMapper partyViewMapper;


    public PartyView execute(AddMemberToAreaPartyCommand command) {
        var hero = heroFacade.getHeroById(command.memberId());
        var area = areaFacade.getAreaById(command.areaId());

        if (Boolean.FALSE.equals(area.isUnlocked())) {
            throw new AreaLockedException(area.id());
        }
        var partyOptional = partyRepository.findByAreaId(area.id());

        var party = partyOptional.orElseGet(() -> Party.forArea(area.id()));
        party.addMember(hero.getId());
        partyRepository.save(party);

        return partyViewMapper.from(party);
    }
}
