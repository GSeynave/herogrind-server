package com.shock.herogrind.party.internal.application;

import com.shock.herogrind.party.internal.application.get.PartyView;
import com.shock.herogrind.party.internal.domain.Party;
import com.shock.herogrind.party.internal.domain.PartyAssignment;
import com.shock.herogrind.party.internal.domain.PartyType;

import java.util.ArrayList;
import java.util.UUID;

public class PartyFactory {

    public static Party party(){
        return Party.builder()
                .id(UUID.randomUUID())
                .name("Party name")
                .maxSize(4)
                .assignment(PartyAssignment.active())
                .members(new ArrayList<>())
                .build();
    }

    public static PartyView partyView(){
        return PartyView.builder()
                .id(UUID.randomUUID())
                .name("Party name")
                .maxSize(4)
                .areaId(UUID.randomUUID())
                .partyType(PartyType.ACTIVE.name())
                .members(new ArrayList<>())
                .build();
    }
}
