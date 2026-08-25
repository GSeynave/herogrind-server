package com.shock.herogrind.party.internal.application;

import com.shock.herogrind.party.api.exception.ActivePartyNotFoundException;
import com.shock.herogrind.party.internal.domain.PartyRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class RemoveMemberFromAreaPartyUseCase {

    private final PartyRepository partyRepository;

    public void execute(RemoveMemberFromAreaPartyCommand command) {
        var party = partyRepository.findByAreaId(command.areaId()).orElseThrow(
                ActivePartyNotFoundException::new
        );

        party.removeMember(command.memberId());

        if (party.isEmpty())
            partyRepository.delete(party.getId());
        else
            partyRepository.save(party);
    }
}