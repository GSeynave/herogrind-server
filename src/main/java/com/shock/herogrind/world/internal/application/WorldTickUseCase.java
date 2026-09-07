package com.shock.herogrind.world.internal.application;

import com.shock.herogrind.combat.api.CombatFacade;
import com.shock.herogrind.hero.api.HeroFacade;
import com.shock.herogrind.party.api.PartyFacade;
import com.shock.herogrind.party.api.PartyInfo;
import com.shock.herogrind.world.internal.domain.HeroActivity;
import com.shock.herogrind.world.internal.domain.HeroActivityRepository;
import com.shock.herogrind.world.internal.domain.HeroActivityState;
import com.shock.herogrind.world.internal.domain.WorldEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Queue;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class WorldTickUseCase {

    private final HeroFacade heroFacade;
    private final PartyFacade partyFacade;
    private final CombatFacade combatFacade;
    private final HeroActivityRepository heroActivityRepository;
    private final EncounterActivityHandler encounterActivityHandler;

    private final Queue<WorldEvent> worldEventQueue = new ArrayDeque<>();

    public void execute() {
        log.debug("Starting world tick");
        var heroes = heroFacade.getAllHeroes();
        var parties = partyFacade.getPartyInfo();
        log.debug("Processing {} heroes", heroes.size());

        heroes.forEach(h -> {
                    log.trace("Processing hero {}", h.getId());
                    var currentActivity = heroActivityRepository.getOrIdle(h.getId());

                    if (currentActivity.state().equals(HeroActivityState.IN_ENCOUNTER)) {
                        var result = encounterActivityHandler.handle(currentActivity);

                        result.combatActions()
                                .forEach(a -> worldEventQueue.add(WorldEvent.from(a)));

                        updateActivity(currentActivity, result.activity());
                        return;
                    }

                    if (!currentActivity.isReadyForNextActivity()) {
                        return;
                    }

                    var heroParty = parties.stream()
                            .filter(p -> p.members().contains(h.getId()))
                            .findFirst();

                    var nextActivity = resolveActivity(currentActivity, heroParty);
                    updateActivity(currentActivity, nextActivity);

                    if (!currentActivity.state().equals(nextActivity.state())) {
                        worldEventQueue.add(WorldEvent.from(nextActivity));
                    }
                }
        );
    }

    public List<WorldEvent> getEvents() {
        var events = new ArrayList<WorldEvent>();
        WorldEvent event;
        while ((event = worldEventQueue.poll()) != null) {
            events.add(event);
        }
        if (!events.isEmpty()) {
            log.debug("Retrieving {} world events", events.size());
        }
        return events;
    }

    protected HeroActivity resolveActivity(HeroActivity current, Optional<PartyInfo> party) {
        if (party.isEmpty()) {
            log.trace("Hero {} has no party, setting to idle", current.heroId());
            return HeroActivity.idle(current.heroId());
        }

        var heroParty = party.get();

        if (heroParty.partyType().equals("ACTIVE")) {
            log.trace("Hero {} in active party, moving to dungeon", current.heroId());
            return HeroActivity.inDungeon(current.heroId());
        }

        return resolveAreaActivity(current, heroParty.areaId());
    }

    private HeroActivity resolveAreaActivity(HeroActivity current, UUID areaId) {
        return switch (current.state()) {
            case IDLE, RESTING, DUNGEON, IN_ENCOUNTER, DEAD -> HeroActivity.roaming(current.heroId(), areaId);
            case ROAMING -> {
                var encounter = combatFacade.startEncounter(current.heroId(), areaId);
                yield HeroActivity.inEncounter(current.heroId(), areaId, encounter.encounterId());
            }
        };
    }

    private void updateActivity(HeroActivity currentActivity, HeroActivity newActivity) {
        if (currentActivity.equals(newActivity)) {
            return;
        }
        heroActivityRepository.save(newActivity);
        if (currentActivity.state() != newActivity.state()) {
            worldEventQueue.add(WorldEvent.from(newActivity));
        }
    }
}
