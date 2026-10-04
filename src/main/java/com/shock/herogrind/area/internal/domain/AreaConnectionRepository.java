package com.shock.herogrind.area.internal.domain;

import java.util.UUID;

public interface AreaConnectionRepository {
  void save(UUID areaId, UUID connectedAreaId);

}
