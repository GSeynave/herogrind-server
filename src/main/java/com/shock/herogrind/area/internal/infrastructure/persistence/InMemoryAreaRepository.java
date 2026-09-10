package com.shock.herogrind.area.internal.infrastructure.persistence;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Repository;

import com.shock.herogrind.area.internal.domain.Area;
import com.shock.herogrind.area.internal.domain.AreaRepository;

@Repository
public class InMemoryAreaRepository implements AreaRepository {
  // create seed

  private final Map<UUID, Area> areaMap = new HashMap<>();

  @Override
  public Area save(Area area) {
    areaMap.put(area.getId(), area);
    return area;
  }

  @Override
  public List<Area> findAll() {
    return new ArrayList<>(areaMap.values());
  }

  @Override
  public Optional<Area> findById(UUID id) {
    return Optional.ofNullable(areaMap.get(id));
  }
}
