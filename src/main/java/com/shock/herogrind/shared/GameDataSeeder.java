package com.shock.herogrind.shared;

import java.util.UUID;

import jakarta.annotation.PostConstruct;

import org.springframework.stereotype.Component;

import com.shock.herogrind.area.internal.domain.Area;
import com.shock.herogrind.area.internal.domain.AreaRepository;
import com.shock.herogrind.hero.internal.domain.Hero;
import com.shock.herogrind.hero.internal.domain.HeroRepository;
import com.shock.herogrind.hero.internal.domain.HeroRole;
import com.shock.herogrind.monster.internal.application.MonsterRepository;
import com.shock.herogrind.monster.internal.domain.Monster;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class GameDataSeeder {

  private final HeroRepository heroRepository;
  private final AreaRepository areaRepository;
  private final MonsterRepository monsterRepository;

  @PostConstruct
  public void seedGameData() {
    var hero1 = new Hero(UUID.randomUUID(), "Hero 1", HeroRole.MELEE, 1, 50D, 3D, 1D);
    var hero2 = new Hero(UUID.randomUUID(), "Hero 2", HeroRole.MELEE, 1, 50D, 3D, 1D);
    heroRepository.save(hero1);
    heroRepository.save(hero2);

    var darkForest = Area.builder()
        .id(UUID.randomUUID())
        .name("Dark Forest")
        .unlocked(true)
        .build();
    var abandonedMine = Area.builder()
        .id(UUID.randomUUID())
        .name("Abandoned Mine")
        .unlocked(true)
        .build();
    var oldRuins = Area.builder()
        .id(UUID.randomUUID())
        .name("Old Ruins")
        .unlocked(true)
        .build();
    areaRepository.save(darkForest);
    areaRepository.save(abandonedMine);
    areaRepository.save(oldRuins);

    var m1 = new Monster(UUID.randomUUID(), "Goblin", 20D, 1D, darkForest.getId());
    var m2 = new Monster(UUID.randomUUID(), "Wolf", 30D, 2D, abandonedMine.getId());
    var m3 = new Monster(UUID.randomUUID(), "Skeleton", 40D, 3D, oldRuins.getId());
    monsterRepository.save(m1);
    monsterRepository.save(m2);
    monsterRepository.save(m3);
  }
}
