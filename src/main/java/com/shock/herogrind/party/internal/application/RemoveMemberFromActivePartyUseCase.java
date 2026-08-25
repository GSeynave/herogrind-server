package com.shock.herogrind.party.internal.application;

import com.shock.herogrind.party.api.exception.ActivePartyNotFoundException;
import com.shock.herogrind.party.internal.domain.PartyRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class RemoveMemberFromActivePartyUseCase {

    private final PartyRepository partyRepository;

    public void execute(RemoveMemberFromActivePartyCommand command) {
        var party = partyRepository.findActive().orElseThrow(
                ActivePartyNotFoundException::new
        );

        party.removeMember(command.memberId());
        if (party.isEmpty())
            partyRepository.delete(party.getId());
        else
            partyRepository.save(party);
    }
}
