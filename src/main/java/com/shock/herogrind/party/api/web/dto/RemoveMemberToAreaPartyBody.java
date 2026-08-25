package com.shock.herogrind.party.api.web.dto;

import java.util.UUID;

public record RemoveMemberToAreaPartyBody(
        UUID memberId,
        UUID areaId
) {


}
