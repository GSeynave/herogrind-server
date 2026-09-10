package com.shock.herogrind.hero.api;

import java.util.List;
import java.util.UUID;

public interface HeroFacade {

    HeroInfo getHeroInfoById(UUID heroId);
    HeroPartyInfo getHeroById(UUID heroId);
    List<HeroPartyInfo> getAllHeroes();
}
