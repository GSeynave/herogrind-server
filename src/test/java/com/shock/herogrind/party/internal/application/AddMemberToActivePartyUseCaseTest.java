package com.shock.herogrind.party.internal.application;

import com.shock.herogrind.hero.api.HeroFacade;
import com.shock.herogrind.hero.api.HeroPartyInfo;
import com.shock.herogrind.hero.api.exception.HeroNotFoundException;
import com.shock.herogrind.hero.internal.domain.HeroRole;
import com.shock.herogrind.party.internal.application.get.PartyView;
import com.shock.herogrind.party.internal.application.get.PartyViewMapper;
import com.shock.herogrind.party.internal.domain.Party;
import com.shock.herogrind.party.internal.domain.PartyRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AddMemberToActivePartyUseCaseTest {

    @Mock
    private PartyRepository partyRepository;
    @Mock
    private HeroFacade heroFacade;
    @Mock
    private PartyViewMapper partyViewMapper;
    @InjectMocks
    private AddMemberToActivePartyUseCase useCase;

    @Test
    void heroDoesNotExist() {
        var uuid = UUID.randomUUID();
        when(heroFacade.getHeroById(uuid)).thenThrow(HeroNotFoundException.class);

        var command = new AddMemberToActivePartyCommand(uuid);

        Assertions.assertThrows(HeroNotFoundException.class, () -> useCase.execute(command));
    }

    @Test
    void noActiveParty() {
        var uuid = UUID.randomUUID();
        var heroPartyInfo = new HeroPartyInfo(uuid, "hero test", HeroRole.MELEE, 1);
        when(heroFacade.getHeroById(uuid)).thenReturn(heroPartyInfo);
        when(partyRepository.findActive()).thenReturn(Optional.empty());
        when(partyViewMapper.from(any())).thenReturn(mock(PartyView.class));

        var command = new AddMemberToActivePartyCommand(uuid);

        var result = useCase.execute(command);

        verify(partyRepository).save(any(Party.class));
        Assertions.assertNotNull(result);
    }
}