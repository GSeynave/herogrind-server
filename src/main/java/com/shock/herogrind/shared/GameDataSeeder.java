package com.shock.herogrind.shared;

import java.util.UUID;

import jakarta.annotation.PostConstruct;

import org.springframework.stereotype.Component;

import com.shock.herogrind.area.internal.domain.Area;
import com.shock.herogrind.area.internal.domain.AreaConnectionRepository;
import com.shock.herogrind.area.internal.domain.AreaRepository;
import com.shock.herogrind.area.internal.domain.MapPosition;
import com.shock.herogrind.area.internal.domain.Size;
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
  private final AreaConnectionRepository areaConnectionRepository;
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
        .mapPosition(MapPosition.builder().x(5).y(0).build())
        .size(Size.builder().width(4).height(3).build())
        .build();
    var abandonedMine = Area.builder()
        .id(UUID.randomUUID())
        .name("Abandoned Mine")
        .unlocked(true)
        .mapPosition(MapPosition.builder().x(0).y(4).build())
        .size(Size.builder().width(4).height(3).build())
        .build();
    var oldRuins = Area.builder()
        .id(UUID.randomUUID())
        .name("Old Ruins")
        .unlocked(true)
        .mapPosition(MapPosition.builder().x(5).y(4).build())
        .size(Size.builder().width(4).height(3).build())
        .build();
    var town = Area.builder()
        .id(UUID.randomUUID())
        .name("Town")
        .unlocked(true)
        .mapPosition(MapPosition.builder().x(0).y(0).build())
        .size(Size.builder().width(4).height(3).build())
        .build();
    var graveyard = Area.builder()
        .id(UUID.randomUUID())
        .name("Graveyard")
        .unlocked(true)
        .mapPosition(MapPosition.builder().x(8).y(4).build())
        .size(Size.builder().width(4).height(3).build())
        .build();
    areaRepository.save(darkForest);
    areaRepository.save(abandonedMine);
    areaRepository.save(oldRuins);
    areaRepository.save(town);
    areaRepository.save(graveyard);

    areaConnectionRepository.save(town.getId(), darkForest.getId());
    areaConnectionRepository.save(town.getId(), abandonedMine.getId());

    areaConnectionRepository.save(darkForest.getId(), oldRuins.getId());
    areaConnectionRepository.save(darkForest.getId(), town.getId());

    areaConnectionRepository.save(abandonedMine.getId(), town.getId());
    areaConnectionRepository.save(abandonedMine.getId(), oldRuins.getId());

    areaConnectionRepository.save(oldRuins.getId(), darkForest.getId());
    areaConnectionRepository.save(oldRuins.getId(), abandonedMine.getId());
    areaConnectionRepository.save(oldRuins.getId(), graveyard.getId());

    var m1 = new Monster(UUID.randomUUID(), "Goblin", 20D, 1D, darkForest.getId());
    var m2 = new Monster(UUID.randomUUID(), "Wolf", 30D, 2D, abandonedMine.getId());
    var m3 = new Monster(UUID.randomUUID(), "Skeleton", 40D, 3D, oldRuins.getId());
    monsterRepository.save(m1);
    monsterRepository.save(m2);
    monsterRepository.save(m3);
  }
}
