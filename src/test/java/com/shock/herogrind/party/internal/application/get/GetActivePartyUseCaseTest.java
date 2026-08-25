package com.shock.herogrind.party.internal.application.get;

import com.shock.herogrind.party.api.exception.ActivePartyNotFoundException;
import com.shock.herogrind.party.internal.application.PartyFactory;
import com.shock.herogrind.party.internal.domain.PartyRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetActivePartyUseCaseTest {

    @Mock
    private PartyRepository partyRepository;
    @Mock
    private PartyViewMapper partyViewMapper;
    @InjectMocks
    GetActivePartyUseCase useCase;


    @Test
    void noActiveParty(){
        when(partyRepository.findActive()).thenReturn(Optional.empty());

        Assertions.assertThrows(ActivePartyNotFoundException.class, ()->useCase.execute());
    }

    @Test
    void partyFound(){
        var party = PartyFactory.party();
        var partyView = PartyFactory.partyView();
        when(partyViewMapper.from(party)).thenReturn(partyView);
        when(partyRepository.findActive()).thenReturn(Optional.of(party));

        var result = useCase.execute();

        Assertions.assertNotNull(result);
    }
}