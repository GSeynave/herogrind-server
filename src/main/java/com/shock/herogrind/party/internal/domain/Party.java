package com.shock.herogrind.party.internal.domain;

import com.shock.herogrind.party.api.exception.HeroAlreadyInPartyException;
import com.shock.herogrind.party.api.exception.PartyFullException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Builder
@AllArgsConstructor
public class Party {

    private static final int DEFAULT_DUNGEON_PARTY_MAX_SIZE = 4;

    private UUID id;
    private String name;
    private int maxSize;
    private List<UUID> members;
    private PartyAssignment assignment;

    public void addMember(UUID heroId) {
        if (members.contains(heroId))
            throw new HeroAlreadyInPartyException(id);
        if (members.size() >= maxSize)
            throw new PartyFullException(id);
        this.members.add(heroId);
    }

    public void removeMember(UUID heroId) {
        this.members.remove(heroId);
    }

    public static Party active() {
        return new Party(
                UUID.randomUUID(),
                "Active Party",
                DEFAULT_DUNGEON_PARTY_MAX_SIZE,
                new ArrayList<>(),
                PartyAssignment.active()
        );
    }

    public static Party forArea(UUID areaId) {
        return new Party(
                UUID.randomUUID(),
                "Area " + areaId + " Party",
                DEFAULT_DUNGEON_PARTY_MAX_SIZE,
                new ArrayList<>(),
                PartyAssignment.area(areaId));
    }

    public boolean isEmpty(){
        return this.members.isEmpty();
    }
}
