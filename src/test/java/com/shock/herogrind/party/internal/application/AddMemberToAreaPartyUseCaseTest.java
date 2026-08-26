package com.shock.herogrind.party.internal.application;

import com.shock.herogrind.area.api.AreaFacade;
import com.shock.herogrind.area.api.AreaInfo;
import com.shock.herogrind.area.api.exception.AreaNotFoundException;
import com.shock.herogrind.hero.api.HeroFacade;
import com.shock.herogrind.hero.api.HeroPartyInfo;
import com.shock.herogrind.hero.api.exception.HeroNotFoundException;
import com.shock.herogrind.hero.internal.domain.HeroRole;
import com.shock.herogrind.party.api.exception.AreaLockedException;
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
class AddMemberToAreaPartyUseCaseTest {
    @Mock
    private PartyRepository partyRepository;
    @Mock
    private HeroFacade heroFacade;
    @Mock
    private AreaFacade areaFacade;
    @Mock
    private PartyViewMapper partyViewMapper;
    @InjectMocks
    private AddMemberToAreaPartyUseCase useCase;

    @Test
    void heroDoesNotExist() {
        var heroId = UUID.randomUUID();
        var areaId = UUID.randomUUID();
        when(heroFacade.getHeroById(heroId)).thenThrow(HeroNotFoundException.class);

        var command = new AddMemberToAreaPartyCommand(heroId, areaId);

        Assertions.assertThrows(HeroNotFoundException.class, () -> useCase.execute(command));
    }

    @Test
    void areaDoesNotExist() {
        var heroId = UUID.randomUUID();
        var areaId = UUID.randomUUID();
        var heroPartyInfo = new HeroPartyInfo(heroId, "hero test", HeroRole.MELEE, 1);
        when(heroFacade.getHeroById(heroId)).thenReturn(heroPartyInfo);
        when(areaFacade.getAreaById(areaId)).thenThrow(new AreaNotFoundException(areaId));

        var command = new AddMemberToAreaPartyCommand(heroId, areaId);

        Assertions.assertThrows(AreaNotFoundException.class, () -> useCase.execute(command));
    }

    @Test
    void areaLocked() {
        var heroId = UUID.randomUUID();
        var areaId = UUID.randomUUID();
        var heroPartyInfo = new HeroPartyInfo(heroId, "hero test", HeroRole.MELEE, 1);
        var areaInfo =new AreaInfo(areaId, "area test", false);
        when(heroFacade.getHeroById(heroId)).thenReturn(heroPartyInfo);
        when(areaFacade.getAreaById(areaId)).thenReturn(areaInfo);

        var command = new AddMemberToAreaPartyCommand(heroId, areaId);

        Assertions.assertThrows(AreaLockedException.class, () -> useCase.execute(command));
    }

    @Test
    void noActiveParty() {
        var heroId = UUID.randomUUID();
        var areaId = UUID.randomUUID();
        var heroPartyInfo = new HeroPartyInfo(heroId, "hero test", HeroRole.MELEE, 1);
        var areaInfo =new AreaInfo(areaId, "area test", true);
        when(heroFacade.getHeroById(heroId)).thenReturn(heroPartyInfo);
        when(areaFacade.getAreaById(areaId)).thenReturn(areaInfo);
        when(partyRepository.findByAreaId(areaId)).thenReturn(Optional.empty());
        when(partyViewMapper.from(any())).thenReturn(mock(PartyView.class));

        var command = new AddMemberToAreaPartyCommand(heroId, areaId);

        var result = useCase.execute(command);

        verify(partyRepository).save(any(Party.class));
        Assertions.assertNotNull(result);
    }

}