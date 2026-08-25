package com.shock.herogrind.hero.api;

import java.util.UUID;

public interface HeroFacade {

    HeroPartyInfo getHeroById(UUID heroId);
}
