package com.shock.herogrind.world.internal.domain;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.stereotype.Repository;

import com.shock.herogrind.area.api.AreaFacade;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class InMemoryHeroActivityRepository implements HeroActivityRepository {
  private final Map<UUID, HeroActivity> heroActivityMap = new HashMap<>();

  private final AreaFacade areaFacade;

  @Override
  public HeroActivity getOrIdle(UUID heroId) {
    var town = areaFacade.getAreas().stream().filter(area -> area.isTown()).findFirst()
        .orElseThrow(() -> new IllegalStateException("No town area found"));

    var now = Instant.now();
    return heroActivityMap.getOrDefault(
        heroId,
        new HeroActivity(
            heroId,
            town.id(),
            HeroActivityState.IDLE,
            now.toEpochMilli(),
            now.plus(0, ChronoUnit.SECONDS).toEpochMilli()));
  }

  @Override
  public void save(HeroActivity activity) {
    heroActivityMap.put(activity.heroId(), activity);

  }

  @Override
  public List<HeroActivity> findAll() {
    return heroActivityMap.values().stream().toList();
  }
}
