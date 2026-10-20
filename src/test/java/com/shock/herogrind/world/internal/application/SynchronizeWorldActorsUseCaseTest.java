package com.shock.herogrind.world.internal.application;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import com.shock.herogrind.hero.api.HeroFacade;
import com.shock.herogrind.hero.api.HeroInfo;
import com.shock.herogrind.hero.api.HeroPartyInfo;
import com.shock.herogrind.hero.internal.domain.HeroRole;
import com.shock.herogrind.world.internal.domain.ActorType;
import com.shock.herogrind.world.internal.domain.HeroActivity;
import com.shock.herogrind.world.internal.domain.HeroActivityRepository;
import com.shock.herogrind.world.internal.domain.HeroActivityState;
import com.shock.herogrind.world.internal.domain.InMemoryWorldActorRepository;
import com.shock.herogrind.world.internal.domain.WorldActor;
import com.shock.herogrind.world.internal.domain.WorldActorBehavior;
import com.shock.herogrind.world.internal.domain.WorldPosition;

class SynchronizeWorldActorsUseCaseTest {

  @Test
  void createsAnActorForAnIdleHeroThatDoesNotAlreadyHaveOne() {
    var heroId = UUID.randomUUID();
    var areaId = UUID.randomUUID();
    var actorRepository = new InMemoryWorldActorRepository();
    var useCase = new SynchronizeWorldActorsUseCase(
        new FixedHeroActivityRepository(List.of(
            new HeroActivity(heroId, areaId, HeroActivityState.IDLE, 10L, 20L))),
        new FixedHeroFacade(new HeroInfo(heroId, "Archer", 50D, 8D)),
        actorRepository);

    useCase.synchronizeHeroes();

    var actor = actorRepository.findById(heroId).orElseThrow();
    assertEquals(heroId, actor.id());
    assertEquals(areaId, actor.areaId());
    assertEquals(ActorType.HERO, actor.type());
    assertEquals(new WorldPosition(0.5, 0.5), actor.position());
    assertEquals(WorldActorBehavior.ROAMING, actor.behavior());
  }

  @Test
  void preservesAnExistingActorThatIsAlreadyPursuingWhileLegacyActivityIsIdle() {
    var heroId = UUID.randomUUID();
    var areaId = UUID.randomUUID();
    var existingActor = new WorldActor(
        heroId, ActorType.HERO, areaId, new WorldPosition(0.7, 0.4), new WorldPosition(0.8, 0.4),
        com.shock.herogrind.world.internal.domain.WorldActorBehavior.PURSUING, UUID.randomUUID(),
        12D, 50D, 8D, 0.05, 0.2, 0.12, 200L);
    var actorRepository = new InMemoryWorldActorRepository();
    actorRepository.save(existingActor);
    var useCase = new SynchronizeWorldActorsUseCase(
        new FixedHeroActivityRepository(List.of(
            new HeroActivity(heroId, areaId, HeroActivityState.IDLE, 10L, 20L))),
        new FixedHeroFacade(new HeroInfo(heroId, "Archer", 50D, 8D)),
        actorRepository);

    useCase.synchronizeHeroes();

    assertEquals(existingActor, actorRepository.findById(heroId).orElseThrow());
  }

  @Test
  void updatesAnExistingHeroActorToItsGhostState() {
    var actorRepository = new InMemoryWorldActorRepository();
    var heroId = UUID.randomUUID();
    actorRepository.save(new WorldActor(
        heroId, ActorType.HERO, UUID.randomUUID(), new WorldPosition(0.5, 0.5), new WorldPosition(0.5, 0.5),
        com.shock.herogrind.world.internal.domain.WorldActorBehavior.ROAMING, null,
        50D, 50D, 8D, 0.05, 0.2, 0.12, 0));
    var useCase = new SynchronizeWorldActorsUseCase(
        new FixedHeroActivityRepository(List.of(
            new HeroActivity(heroId, UUID.randomUUID(), HeroActivityState.GHOST_TRAVELING, 10L, 20L))),
        new FixedHeroFacade(new HeroInfo(heroId, "Archer", 50D, 8D)),
        actorRepository);

    useCase.synchronizeHeroes();

    assertEquals(
        com.shock.herogrind.world.internal.domain.WorldActorBehavior.GHOST_TRAVELING,
        actorRepository.findById(heroId).orElseThrow().behavior());
    assertNull(actorRepository.findById(heroId).orElseThrow().targetId());
  }

  @Test
  void restoresAResurrectedHeroToFullHealthAndRoaming() {
    var heroId = UUID.randomUUID();
    var townId = UUID.randomUUID();
    var actorRepository = new InMemoryWorldActorRepository();
    actorRepository.save(new WorldActor(
        heroId, ActorType.HERO, UUID.randomUUID(), new WorldPosition(0.5, 0.5), new WorldPosition(0.5, 0.5),
        WorldActorBehavior.GHOST_RESURRECTING, null,
        0D, 50D, 8D, 0.05, 0.2, 0.12, 0));
    var useCase = new SynchronizeWorldActorsUseCase(
        new FixedHeroActivityRepository(List.of(
            new HeroActivity(heroId, townId, HeroActivityState.IDLE, 10L, 20L))),
        new FixedHeroFacade(new HeroInfo(heroId, "Archer", 50D, 8D)),
        actorRepository);

    useCase.synchronizeHeroes();

    var actor = actorRepository.findById(heroId).orElseThrow();
    assertEquals(townId, actor.areaId());
    assertEquals(50D, actor.health());
    assertEquals(WorldActorBehavior.ROAMING, actor.behavior());
  }

  private static final class FixedHeroActivityRepository implements HeroActivityRepository {
    private final List<HeroActivity> activities;

    private FixedHeroActivityRepository(List<HeroActivity> activities) {
      this.activities = new ArrayList<>(activities);
    }

    @Override
    public HeroActivity getOrIdle(UUID heroId) {
      return activities.stream()
          .filter(activity -> activity.heroId().equals(heroId))
          .findFirst()
          .orElseThrow();
    }

    @Override
    public void save(HeroActivity activity) {
      activities.add(activity);
    }

    @Override
    public List<HeroActivity> findAll() {
      return activities;
    }
  }

  private static final class FixedHeroFacade implements HeroFacade {
    private final HeroInfo hero;

    private FixedHeroFacade(HeroInfo hero) {
      this.hero = hero;
    }

    @Override
    public HeroInfo getHeroInfoById(UUID heroId) {
      return hero;
    }

    @Override
    public HeroPartyInfo getHeroById(UUID heroId) {
      throw new UnsupportedOperationException();
    }

    @Override
    public List<HeroPartyInfo> getAllHeroes() {
      return List.of(new HeroPartyInfo(hero.id(), hero.name(), HeroRole.MELEE, 1));
    }
  }
}
