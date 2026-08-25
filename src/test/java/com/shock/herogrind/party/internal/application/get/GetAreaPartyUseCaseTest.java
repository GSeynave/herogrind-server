package com.shock.herogrind.party.internal.application.get;

import com.shock.herogrind.party.api.exception.AreaPartyNotFoundException;
import com.shock.herogrind.party.internal.application.PartyFactory;
import com.shock.herogrind.party.internal.domain.PartyRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetAreaPartyUseCaseTest {
    @Mock
    private PartyRepository partyRepository;
    @Mock
    private PartyViewMapper partyViewMapper;
    @InjectMocks
    GetAreaPartyUseCase useCase;

    @Test
    void noAreaParty(){
        var uuid = UUID.randomUUID();
        var query =new GetAreaPartyQuery(uuid);
        when(partyRepository.findByAreaId(uuid)).thenReturn(Optional.empty());

        Assertions.assertThrows(AreaPartyNotFoundException.class, ()->useCase.execute(query));
    }

    @Test
    void areaPartyFound(){
        var party = PartyFactory.party();
        var partyView = PartyFactory.partyView();
        var uuid = UUID.randomUUID();
        var query =new GetAreaPartyQuery(uuid);
        when(partyRepository.findByAreaId(uuid)).thenReturn(Optional.of(party));
        when(partyViewMapper.from(party)).thenReturn(partyView);

        var result = useCase.execute(query);

        Assertions.assertNotNull(result);
    }

}