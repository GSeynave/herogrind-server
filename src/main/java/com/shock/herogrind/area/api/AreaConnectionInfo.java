package com.shock.herogrind.area.api;

import java.util.List;
import java.util.UUID;

public record AreaConnectionInfo(UUID areaId, List<UUID> connections) {

}
