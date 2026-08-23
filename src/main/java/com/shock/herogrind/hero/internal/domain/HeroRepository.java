package com.shock.herogrind.hero.internal.domain;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface HeroRepository {

    List<Hero> findAll();

    Optional<Hero> findById(UUID heroId);

    UUID save(Hero hero);
}
