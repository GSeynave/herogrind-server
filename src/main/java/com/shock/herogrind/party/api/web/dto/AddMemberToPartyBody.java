package com.shock.herogrind.party.api.web.dto;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record AddMemberToPartyBody(
        @NotNull UUID memberId
) {
}
