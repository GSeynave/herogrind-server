package com.shock.herogrind.party.internal.application.get;

import com.shock.herogrind.party.internal.domain.PartyRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class GetPartiesUseCase {

    private final PartyRepository partyRepository;
    private final PartyViewMapper partyViewMapper;

    public List<PartyView> execute() {

        return partyRepository.findAll().stream()
                .map(partyViewMapper::from)
                .toList();
    }
}
