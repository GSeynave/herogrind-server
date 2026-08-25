package com.shock.herogrind.party.api.web.dto;

import com.shock.herogrind.hero.api.HeroPartyInfo;
import com.shock.herogrind.party.internal.application.get.PartyView;

import java.util.List;
import java.util.UUID;

public record PartyDTO(
        UUID id,
        String name,
        int maxSize,
        List<HeroPartyInfo> members,
        UUID areaId,
        String partyType
) {


    public static PartyDTO from(PartyView view) {
        return new PartyDTO(
                view.id(),
                view.name(),
                view.maxSize(),
                view.members(),
                view.areaId(),
                view.partyType()
        );
    }
}
