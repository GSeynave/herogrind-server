package com.shock.herogrind.world.internal.domain;

import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Repository
public class InMemoryHeroActivityRepository implements HeroActivityRepository {
    private final Map<UUID, HeroActivity> heroActivityMap = new HashMap<>();

    @Override
    public HeroActivity getOrIdle(UUID heroId) {
        return heroActivityMap.getOrDefault(
                heroId,
                HeroActivity.idle(heroId)
        );
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
