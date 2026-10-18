package com.shock.herogrind.area.internal.application;

import java.util.List;
import java.util.UUID;

public record AreaConnectionView(UUID areaId, List<UUID> connections) {

}
