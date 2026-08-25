package com.shock.herogrind.party.internal.application;

import java.util.UUID;

public record AddMemberToAreaPartyCommand(UUID memberId, UUID areaId) {
}
