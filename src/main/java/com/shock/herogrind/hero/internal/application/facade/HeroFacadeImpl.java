package com.shock.herogrind.hero.internal.application.facade;

import com.shock.herogrind.hero.api.HeroFacade;
import com.shock.herogrind.hero.api.HeroPartyInfo;
import com.shock.herogrind.hero.internal.application.get.GetHeroQuery;
import com.shock.herogrind.hero.internal.application.get.GetHeroUseCase;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@AllArgsConstructor
public class HeroFacadeImpl implements HeroFacade {

    private final GetHeroUseCase getHeroUseCase;

    @Override
    public HeroPartyInfo getHeroById(UUID heroId) {
        var hero = getHeroUseCase.execute(new GetHeroQuery(heroId));

        return new HeroPartyInfo(
                hero.getId(), hero.getName(), hero.getRole(), hero.getLevel()
        );
    }
}
