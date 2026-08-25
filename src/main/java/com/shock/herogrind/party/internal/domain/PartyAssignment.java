package com.shock.herogrind.party.internal.domain;

import jakarta.annotation.Nonnull;

import java.util.UUID;

public record PartyAssignment(UUID areaId, PartyType type) {

    public static PartyAssignment active() {
        return new PartyAssignment(null, PartyType.ACTIVE);
    }

    public static PartyAssignment area(@Nonnull UUID areaId) {
        return new PartyAssignment(areaId, PartyType.AREA);
    }

}
