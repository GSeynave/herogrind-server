package com.shock.herogrind.hero.internal.application.facade;

import com.shock.herogrind.hero.api.HeroFacade;
import com.shock.herogrind.hero.api.HeroInfo;
import com.shock.herogrind.hero.api.HeroPartyInfo;
import com.shock.herogrind.hero.internal.application.get.GetHeroQuery;
import com.shock.herogrind.hero.internal.application.get.GetHeroUseCase;
import com.shock.herogrind.hero.internal.application.get.GetHeroesUseCase;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class HeroFacadeImpl implements HeroFacade {

    private final GetHeroUseCase getHeroUseCase;
    private final GetHeroesUseCase getHeroesUseCase;

    @Override
    public HeroPartyInfo getHeroById(UUID heroId) {
        var hero = getHeroUseCase.execute(new GetHeroQuery(heroId));

        return new HeroPartyInfo(
                hero.getId(), hero.getName(), hero.getRole(), hero.getLevel()
        );
    }

    @Override
    public List<HeroPartyInfo> getAllHeroes(){
        var heroes = getHeroesUseCase.execute();

        return heroes.stream()
                .map(h ->
                        new HeroPartyInfo(
                                h.getId(), h.getName(), h.getRole(), h.getLevel()
                        )
                )
                .toList();
    }

    @Override
    public HeroInfo getHeroInfoById(UUID heroId) {
        var query = new GetHeroQuery(heroId);
        return HeroInfo.from(getHeroUseCase.execute(query));
    }
}
