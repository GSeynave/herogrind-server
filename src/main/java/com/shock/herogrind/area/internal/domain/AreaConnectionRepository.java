package com.shock.herogrind.area.internal.domain;

import java.util.List;
import java.util.UUID;

import com.shock.herogrind.area.api.AreaConnectionInfo;

public interface AreaConnectionRepository {
  void save(UUID areaId, UUID connectedAreaId);

  List<AreaConnectionInfo> getAllAreasConnections();

}
