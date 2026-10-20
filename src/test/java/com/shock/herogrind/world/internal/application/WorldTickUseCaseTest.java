package com.shock.herogrind.world.internal.application;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import com.shock.herogrind.hero.api.HeroFacade;
import com.shock.herogrind.hero.api.HeroInfo;
import com.shock.herogrind.hero.api.HeroPartyInfo;
import com.shock.herogrind.hero.internal.domain.HeroRole;
import com.shock.herogrind.monster.api.MonsterFacade;
import com.shock.herogrind.monster.api.MonsterInfo;
import com.shock.herogrind.monster.api.MonsterWorldInfo;
import com.shock.herogrind.party.api.PartyFacade;
import com.shock.herogrind.party.api.PartyInfo;
import com.shock.herogrind.world.internal.domain.HeroActivity;
import com.shock.herogrind.world.internal.domain.HeroActivityRepository;
import com.shock.herogrind.world.internal.domain.HeroActivityState;
import com.shock.herogrind.world.internal.domain.InMemoryWorldActorRepository;

class WorldTickUseCaseTest {

  @Test
  void synchronizesEveryHeroAsAnActorBeforeHandlingActivities() {
    var heroId = UUID.randomUUID();
    var areaId = UUID.randomUUID();
    var heroFacade = new SingleHeroFacade(heroId);
    var activityRepository = new IdleHeroActivityRepository(heroId, areaId);
    var actorRepository = new InMemoryWorldActorRepository();
    var synchronizer = new SynchronizeWorldActorsUseCase(
        activityRepository,
        heroFacade,
        actorRepository);
    var monsterSynchronizer = new SynchronizeMonsterActorsUseCase(
        new EmptyMonsterFacade(),
        actorRepository);
    var actorTick = new ActorTickUseCase(
        actorRepository,
        List.of(),
        new CombatResolutionUseCase(actorRepository));
    var deathTransition = new HeroActorDeathTransitionUseCase(
        actorRepository,
        activityRepository,
        new EmptyPartyFacade());
    var tick = new WorldTickUseCase(
        heroFacade,
        activityRepository,
        synchronizer,
        monsterSynchronizer,
        actorTick,
        deathTransition,
        List.of(new IdleActivityHandlerStub()));

    tick.execute();

    assertTrue(actorRepository.findById(heroId).isPresent());
  }

  @Test
  void doesNotRunTheLegacyRoamingHandlerForActorControlledHeroes() {
    var heroId = UUID.randomUUID();
    var areaId = UUID.randomUUID();
    var heroFacade = new SingleHeroFacade(heroId);
    var activityRepository = new RoamingHeroActivityRepository(heroId, areaId);
    var actorRepository = new InMemoryWorldActorRepository();
    var tick = new WorldTickUseCase(
        heroFacade,
        activityRepository,
        new SynchronizeWorldActorsUseCase(activityRepository, heroFacade, actorRepository),
        new SynchronizeMonsterActorsUseCase(new EmptyMonsterFacade(), actorRepository),
        new ActorTickUseCase(actorRepository, List.of(), new CombatResolutionUseCase(actorRepository)),
        new HeroActorDeathTransitionUseCase(actorRepository, activityRepository, new EmptyPartyFacade()),
        List.of());

    tick.execute();

    assertTrue(actorRepository.findById(heroId).isPresent());
  }

  private static final class EmptyMonsterFacade implements MonsterFacade {
    @Override
    public MonsterInfo getMonsterInfoByAreaId(UUID areaId) {
      throw new UnsupportedOperationException();
    }

    @Override
    public MonsterInfo getMonsterInfoById(UUID monsterId) {
      throw new UnsupportedOperationException();
    }

    @Override
    public List<MonsterInfo> findAll() {
      return List.of();
    }

    @Override
    public List<MonsterWorldInfo> findAllForWorld() {
      return List.of();
    }
  }

  private static final class EmptyPartyFacade implements PartyFacade {
    @Override
    public List<PartyInfo> getPartyInfo() {
      return List.of();
    }

    @Override
    public void removeHeroFromParty(UUID heroId, UUID areaId) {
    }

    @Override
    public java.util.Optional<PartyInfo> getPartyInfoByHeroId(UUID heroId) {
      return java.util.Optional.empty();
    }
  }

  private static final class IdleActivityHandlerStub implements ActivityHandler {
    @Override
    public ActivityHandlerResult handle(HeroActivity currentActivity) {
      return ActivityHandlerResult.empty();
    }

    @Override
    public boolean supports(HeroActivityState state) {
      return state == HeroActivityState.IDLE;
    }
  }

  private static final class IdleHeroActivityRepository implements HeroActivityRepository {
    private final HeroActivity idleActivity;

    private IdleHeroActivityRepository(UUID heroId, UUID areaId) {
      idleActivity = new HeroActivity(heroId, areaId, HeroActivityState.IDLE, 0L, 0L);
    }

    @Override
    public HeroActivity getOrIdle(UUID heroId) {
      return idleActivity;
    }

    @Override
    public void save(HeroActivity activity) {
    }

    @Override
    public List<HeroActivity> findAll() {
      return List.of();
    }
  }

  private static final class RoamingHeroActivityRepository implements HeroActivityRepository {
    private final HeroActivity roamingActivity;

    private RoamingHeroActivityRepository(UUID heroId, UUID areaId) {
      roamingActivity = new HeroActivity(heroId, areaId, HeroActivityState.ROAMING, 0L, 0L);
    }

    @Override
    public HeroActivity getOrIdle(UUID heroId) {
      return roamingActivity;
    }

    @Override
    public void save(HeroActivity activity) {
    }

    @Override
    public List<HeroActivity> findAll() {
      return List.of(roamingActivity);
    }
  }

  private static final class SingleHeroFacade implements HeroFacade {
    private final UUID heroId;

    private SingleHeroFacade(UUID heroId) {
      this.heroId = heroId;
    }

    @Override
    public HeroInfo getHeroInfoById(UUID requestedHeroId) {
      return new HeroInfo(requestedHeroId, "Hero", 50D, 8D);
    }

    @Override
    public HeroPartyInfo getHeroById(UUID requestedHeroId) {
      return new HeroPartyInfo(requestedHeroId, "Hero", HeroRole.MELEE, 1);
    }

    @Override
    public List<HeroPartyInfo> getAllHeroes() {
      return List.of(new HeroPartyInfo(heroId, "Hero", HeroRole.MELEE, 1));
    }
  }
}
