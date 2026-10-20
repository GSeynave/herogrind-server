package com.shock.herogrind.world.internal.application;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import com.shock.herogrind.party.api.PartyFacade;
import com.shock.herogrind.party.api.PartyInfo;
import com.shock.herogrind.world.internal.domain.ActorCombatAction;
import com.shock.herogrind.world.internal.domain.ActorType;
import com.shock.herogrind.world.internal.domain.HeroActivity;
import com.shock.herogrind.world.internal.domain.HeroActivityRepository;
import com.shock.herogrind.world.internal.domain.HeroActivityState;
import com.shock.herogrind.world.internal.domain.InMemoryWorldActorRepository;
import com.shock.herogrind.world.internal.domain.WorldActor;
import com.shock.herogrind.world.internal.domain.WorldActorBehavior;
import com.shock.herogrind.world.internal.domain.WorldPosition;

class HeroActorDeathTransitionUseCaseTest {

  @Test
  void transitionsADeadHeroOnceWhenSeveralActionsKillItInTheSameTick() {
    var heroId = UUID.randomUUID();
    var areaId = UUID.randomUUID();
    var actorRepository = new InMemoryWorldActorRepository();
    actorRepository.save(new WorldActor(
        heroId, ActorType.HERO, areaId, new WorldPosition(0.5, 0.5), new WorldPosition(0.5, 0.5),
        WorldActorBehavior.DEAD, null, 0D, 50D, 8D, 0.05, 0.2, 0.12, 0));
    var activityRepository = new FixedHeroActivityRepository(
        new HeroActivity(heroId, areaId, HeroActivityState.ROAMING, 0L, 0L));
    var partyFacade = new RecordingPartyFacade();
    var useCase = new HeroActorDeathTransitionUseCase(actorRepository, activityRepository, partyFacade);

    var events = useCase.transitionDefeatedHeroes(List.of(
        new ActorCombatAction(UUID.randomUUID(), heroId, 3D, 0D),
        new ActorCombatAction(UUID.randomUUID(), heroId, 4D, 0D)));

    assertEquals(1, events.size());
    assertEquals(1, partyFacade.removalCount);
    assertEquals(HeroActivityState.DYING, activityRepository.savedActivity.state());
    assertEquals(WorldActorBehavior.DYING, actorRepository.findById(heroId).orElseThrow().behavior());
  }

  private static final class FixedHeroActivityRepository implements HeroActivityRepository {
    private final HeroActivity activity;
    private HeroActivity savedActivity;

    private FixedHeroActivityRepository(HeroActivity activity) {
      this.activity = activity;
    }

    @Override
    public HeroActivity getOrIdle(UUID heroId) {
      return activity;
    }

    @Override
    public void save(HeroActivity nextActivity) {
      savedActivity = nextActivity;
    }

    @Override
    public List<HeroActivity> findAll() {
      return List.of(activity);
    }
  }

  private static final class RecordingPartyFacade implements PartyFacade {
    private int removalCount;

    @Override
    public List<PartyInfo> getPartyInfo() {
      return List.of();
    }

    @Override
    public void removeHeroFromParty(UUID heroId, UUID areaId) {
      removalCount++;
    }

    @Override
    public Optional<PartyInfo> getPartyInfoByHeroId(UUID heroId) {
      return Optional.empty();
    }
  }
}
