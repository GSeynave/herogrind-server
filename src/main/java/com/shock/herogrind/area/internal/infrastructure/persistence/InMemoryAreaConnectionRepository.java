package com.shock.herogrind.area.internal.infrastructure.persistence;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import jakarta.annotation.PostConstruct;

import org.springframework.stereotype.Repository;

import com.shock.herogrind.area.internal.domain.Area;
import com.shock.herogrind.area.internal.domain.AreaConnectionRepository;

@Repository
public class InMemoryAreaConnectionRepository implements AreaConnectionRepository {
  // create seed

  private final Map<UUID, List<UUID>> areaConnectionsMap = new HashMap<>();

  @Override
  public void save(UUID areaId, UUID connectedAreaId) {
    var connections = areaConnectionsMap.getOrDefault(areaId, new ArrayList<>());
    connections.add(connectedAreaId);
    areaConnectionsMap.put(areaId, connections);
  }

}
