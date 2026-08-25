package com.shock.herogrind.party.internal.application.get;

import com.shock.herogrind.hero.api.HeroFacade;
import com.shock.herogrind.party.internal.domain.Party;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@AllArgsConstructor
public class PartyViewMapper {
    private final HeroFacade heroFacade;


    public PartyView from(Party party) {
        var heroesPartyInfo = party.getMembers().stream()
                .filter(Objects::nonNull)
                .map(heroFacade::getHeroById)
                .toList();

        return new PartyView(
                party.getId(),
                party.getName(),
                party.getMaxSize(),
                heroesPartyInfo,
                party.getAssignment().areaId(),
                party.getAssignment().type().name()
        );

    }
}
