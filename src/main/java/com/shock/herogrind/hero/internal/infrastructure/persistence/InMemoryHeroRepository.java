package com.shock.herogrind.hero.internal.infrastructure.persistence;

import com.shock.herogrind.hero.internal.domain.Hero;
import com.shock.herogrind.hero.internal.domain.HeroRepository;
import com.shock.herogrind.hero.internal.domain.HeroRole;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class InMemoryHeroRepository implements HeroRepository {

    // later i'll inject the jpa repository interface.
    Map<UUID, Hero> heroes = new HashMap<>();

    @PostConstruct
    void initSeed(){
        var hero1 = new Hero(UUID.randomUUID(), "Hero 1", HeroRole.MELEE, 1, 2D, 1D, 1D);
        var hero2 = new Hero(UUID.randomUUID(), "Hero 2", HeroRole.MELEE, 1, 2D, 1D, 1D);
        heroes.put(hero1.getId(), hero1);
        heroes.put(hero2.getId(), hero2);

    }

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
