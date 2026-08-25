package com.shock.herogrind.party.internal.application;

import java.util.UUID;

public record RemoveMemberFromAreaPartyCommand(UUID memberId, UUID areaId) {
}
