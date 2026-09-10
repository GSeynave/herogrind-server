package com.shock.herogrind.party.api;

import java.util.List;
import java.util.UUID;

public record PartyInfo(UUID partyId, List<UUID> members, UUID areaId, String partyType) {

}
