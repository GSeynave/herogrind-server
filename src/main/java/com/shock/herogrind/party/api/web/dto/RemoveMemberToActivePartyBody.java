package com.shock.herogrind.party.api.web.dto;

import java.util.UUID;

public record RemoveMemberToActivePartyBody(
        UUID memberId,
        UUID areaId
) {


}
