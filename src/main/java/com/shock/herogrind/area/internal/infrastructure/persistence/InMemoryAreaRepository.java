package com.shock.herogrind.area.internal.infrastructure.persistence;

import com.shock.herogrind.area.internal.domain.Area;
import com.shock.herogrind.area.internal.domain.AreaRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Repository
public class InMemoryAreaRepository implements AreaRepository {
    // create seed

    private final Map<UUID, Area> areaMap = new HashMap<>();

    @PostConstruct
    public void seed() {
        var darkForest = Area.builder()
                .id(UUID.randomUUID())
                .name("Dark Forest")
                .unlocked(true)
                .build();
        var abandonedMine = Area.builder()
                .id(UUID.randomUUID())
                .name("Abandoned Mine")
                .unlocked(true)
                .build();
        var oldRuins = Area.builder()
                .id(UUID.randomUUID())
                .name("Old Ruins")
                .unlocked(true)
                .build();
        areaMap.put(darkForest.getId(), darkForest);
        areaMap.put(abandonedMine.getId(), abandonedMine);
        areaMap.put(oldRuins.getId(), oldRuins);
    }

    public List<Area> findAll() {
        return new ArrayList<>(areaMap.values());
    }

    public Optional<Area> findById(UUID id) {
        return Optional.ofNullable(areaMap.get(id));
    }
}
