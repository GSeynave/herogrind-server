package com.shock.herogrind.area.api;

import java.util.List;
import java.util.UUID;

public interface AreaFacade {

    List<AreaInfo> getAreas();
    AreaInfo getAreaById(UUID uuid);
}
