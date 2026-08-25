package com.shock.herogrind.hero.internal.application.get;

import com.shock.herogrind.hero.api.exception.HeroNotFoundException;
import com.shock.herogrind.hero.internal.domain.HeroRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class GetHeroUseCase {

    private final HeroRepository heroRepository;

    public HeroDetailsView execute(GetHeroQuery query) {
        var hero = heroRepository.findById(query.heroId()).orElseThrow(
                () -> new HeroNotFoundException(query.heroId())
        );
        return HeroDetailsView.from(hero);
    }
}
