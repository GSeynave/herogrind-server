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
        .mapPosition(MapPosition.builder().x(11).y(0).build())
        .size(Size.builder().width(10).height(8).build())
        .build();
    var abandonedMine = Area.builder()
        .id(UUID.randomUUID())
        .name("Abandoned Mine")
        .unlocked(true)
        .mapPosition(MapPosition.builder().x(0).y(9).build())
        .size(Size.builder().width(10).height(8).build())
        .build();
    var oldRuins = Area.builder()
        .id(UUID.randomUUID())
        .name("Old Ruins")
        .unlocked(true)
        .mapPosition(MapPosition.builder().x(11).y(9).build())
        .size(Size.builder().width(10).height(8).build())
        .build();
    var town = Area.builder()
        .id(UUID.randomUUID())
        .name("Town")
        .unlocked(true)
        .mapPosition(MapPosition.builder().x(0).y(0).build())
        .size(Size.builder().width(10).height(8).build())
        .build();
    var graveyard = Area.builder()
        .id(UUID.randomUUID())
        .name("Graveyard")
        .unlocked(true)
        .mapPosition(MapPosition.builder().x(22).y(9).build())
        .size(Size.builder().width(10).height(8).build())
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

    areaConnectionRepository.save(graveyard.getId(), oldRuins.getId());

    seedMonsters(monsterRepository, "Goblin", 20D, 30D, darkForest.getId());
    seedMonsters(monsterRepository, "Wolf", 30D, 2D, abandonedMine.getId());
    seedMonsters(monsterRepository, "Skeleton", 40D, 3D, oldRuins.getId());
  }

  private void seedMonsters(
      MonsterRepository monsterRepository,
      String name,
      double health,
      double attackDamage,
      UUID areaId) {
    for (var instanceNumber = 0; instanceNumber < 3; instanceNumber++) {
      monsterRepository.save(new Monster(UUID.randomUUID(), name, health, attackDamage, areaId));
    }
  }
}
