package com.shock.herogrind.hero.internal.application.create;

import com.shock.herogrind.hero.internal.domain.Hero;
import com.shock.herogrind.hero.internal.domain.HeroRepository;
import com.shock.herogrind.hero.internal.domain.HeroRole;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@AllArgsConstructor
public class UnlockHeroUseCase {

    private final HeroRepository heroRepository;

    public UUID execute(UnlockHeroCommand command){

        var hero = new Hero();
        hero.setId(UUID.randomUUID());
        hero.setName(command.name());
        hero.setLevel(1);
        hero.setDefense(2D);
        hero.setHealth(20D);
        hero.setAttack(4D);
        hero.setRole(HeroRole.MELEE);
        return heroRepository.save(hero);
    }
}
