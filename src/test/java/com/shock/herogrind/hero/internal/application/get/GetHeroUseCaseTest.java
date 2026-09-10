package com.shock.herogrind.hero.internal.application.get;

import com.shock.herogrind.hero.api.exception.HeroNotFoundException;
import com.shock.herogrind.hero.internal.domain.HeroRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetHeroUseCaseTest {

    @Mock
    private HeroRepository heroRepository;
    @InjectMocks
    private GetHeroUseCase useCase;

    @Test
    void getHero_found() {
        var hero = HeroFactory.hero();
        var uuid = UUID.randomUUID();
        var command = new GetHeroQuery(uuid);
        when(heroRepository.findById(uuid)).thenReturn(Optional.of(hero));

        var result = useCase.execute(command);

        assertNotNull(result);
        assertEquals(hero.getId(), result.getId());
        assertEquals(hero.getName(), result.getName());
        assertEquals(hero.getLevel(), result.getLevel());
        assertEquals(hero.getRole(), result.getRole());
        assertEquals(hero.getHealth(), result.getHealth());
        assertEquals(hero.getAttack(), result.getAttackDamage());
        assertEquals(hero.getDefense(), result.getDefense());
    }

    @Test
    void getHero_notFound() {
        var uuid = UUID.randomUUID();
        var command = new GetHeroQuery(uuid);
        when(heroRepository.findById(uuid)).thenReturn(Optional.empty());

        assertThrows(HeroNotFoundException.class, () -> useCase.execute(command));

    }
}