package com.shock.herogrind.area.internal.domain;

import java.util.UUID;

public record AreaConnection(UUID areaId, UUID connectedAreaId) {
}
