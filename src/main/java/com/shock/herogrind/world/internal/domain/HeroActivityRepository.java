package com.shock.herogrind.world.internal.domain;


import java.util.List;
import java.util.UUID;

public interface HeroActivityRepository {
    HeroActivity getOrIdle(UUID heroId);
    void save (HeroActivity activity);
    List<HeroActivity> findAll();
}
