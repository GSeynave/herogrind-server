package com.shock.herogrind.combat.api;

import java.util.List;
import java.util.UUID;

public record GhostInfo(
    List<UUID> pathToTown,
    Long arrivalAt,
    Long resurrectionEndAt) {
}
