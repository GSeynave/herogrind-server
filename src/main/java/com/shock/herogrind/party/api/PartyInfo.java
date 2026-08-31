package com.shock.herogrind.party.api;

import com.shock.herogrind.party.internal.domain.PartyType;

import java.util.List;
import java.util.UUID;

public record PartyInfo(UUID partyId, List<UUID> members, UUID areaId, String partyType) {


}
