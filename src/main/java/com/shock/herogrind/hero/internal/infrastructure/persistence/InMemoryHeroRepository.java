package com.shock.herogrind.hero.internal.infrastructure.persistence;

import com.shock.herogrind.hero.internal.domain.Hero;
import com.shock.herogrind.hero.internal.domain.HeroRepository;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class InMemoryHeroRepository implements HeroRepository {

    // later i'll inject the jpa repository interface.
    Map<UUID, Hero> heroes = new HashMap<>();

    @Override
    public List<Hero> findAll() {
        // fake hero list for now before futur persistence layer.
        // normally i would map entities found into domain objects.
        return new ArrayList<>(heroes.values());
    }

    @Override
    public Optional<Hero> findById(UUID heroId) {
        return Optional.ofNullable(heroes.get(heroId));
    }

    @Override
    public UUID save(Hero hero) {
        heroes.put(hero.getId(), hero);
        return hero.getId();
    }
}
