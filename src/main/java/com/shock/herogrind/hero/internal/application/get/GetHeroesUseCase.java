package com.shock.herogrind.hero.internal.application.get;

import com.shock.herogrind.hero.internal.domain.HeroRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class GetHeroesUseCase {

    private final HeroRepository heroRepository;

    public List<HeroView> execute(){
        // later potential Query carrying user informatioon for filter context..

        var heroes =  heroRepository.findAll();
        return heroes.stream().map(HeroView::from).toList();
    }
}
