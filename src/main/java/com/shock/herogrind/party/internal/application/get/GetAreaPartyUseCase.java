package com.shock.herogrind.party.internal.application.get;

import com.shock.herogrind.party.api.exception.AreaPartyNotFoundException;
import com.shock.herogrind.party.internal.domain.PartyRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class GetAreaPartyUseCase {


    private final PartyRepository partyRepository;
    private final PartyViewMapper partyViewMapper;

    public PartyView execute(GetAreaPartyQuery query) {
        var party = partyRepository.findByAreaId(query.areaId()).orElseThrow(
                () -> new AreaPartyNotFoundException(query.areaId())
        );

        return  partyViewMapper.from(party);
    }
}
