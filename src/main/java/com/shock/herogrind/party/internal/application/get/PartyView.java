package com.shock.herogrind.party.internal.application.get;

import com.shock.herogrind.hero.api.HeroPartyInfo;
import com.shock.herogrind.party.internal.domain.Party;
import lombok.Builder;

import java.util.List;
import java.util.UUID;

@Builder
public record PartyView(
        UUID id,
        String name,
        int maxSize,
        List<HeroPartyInfo> members,
        UUID areaId,
        String partyType
) {

    public static PartyView from(Party party, List<HeroPartyInfo> members) {
        return new PartyView(
                party.getId(),
                party.getName(),
                party.getMaxSize(),
                members,
                party.getAssignment().areaId(),
                party.getAssignment().type().name()
        );
    }
}
