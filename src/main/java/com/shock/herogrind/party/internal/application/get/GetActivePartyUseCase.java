package com.shock.herogrind.party.internal.application.get;

import com.shock.herogrind.party.api.exception.ActivePartyNotFoundException;
import com.shock.herogrind.party.internal.domain.PartyRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class GetActivePartyUseCase {

    private final PartyRepository partyRepository;
    private final PartyViewMapper partyViewMapper;

    public PartyView execute() {
        var party = partyRepository.findActive().orElseThrow(
                ActivePartyNotFoundException::new
        );

        return partyViewMapper.from(party);
    }
}
