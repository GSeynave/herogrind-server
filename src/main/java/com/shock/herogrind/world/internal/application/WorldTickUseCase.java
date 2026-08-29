package com.shock.herogrind.world.internal.application;

import com.shock.herogrind.area.api.AreaFacade;
import com.shock.herogrind.hero.api.HeroFacade;
import com.shock.herogrind.party.api.PartyFacade;
import com.shock.herogrind.party.api.PartyInfo;
import com.shock.herogrind.world.internal.domain.HeroActivity;
import com.shock.herogrind.world.internal.domain.HeroActivityRepository;
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
    private final AreaFacade areaFacade;
    private final PartyFacade partyFacade;
    private final HeroActivityRepository heroActivityRepository;

    private final Queue<WorldEvent> worldEventQueue = new ArrayDeque<>();

    public void execute() {
        log.debug("Starting world tick");
        var heroes = heroFacade.getAllHeroes();
        var parties = partyFacade.getPartyInfo();
        log.debug("Processing {} heroes", heroes.size());

        heroes.forEach(h -> {
                    log.trace("Processing hero {}", h.getId());
                    var currentActivity = heroActivityRepository.getOrIdle(h.getId());
                    if (!currentActivity.isReadyForNextActivity()) {
                        log.trace("Hero {} not ready for next activity", h.getId());
                        return;
                    }

                    var heroParty = parties.stream()
                            .filter(p -> p.members().contains(h.getId()))
                            .findFirst();

                    var nextActivity = resolveActivity(currentActivity, heroParty);

                    heroActivityRepository.save(nextActivity);
                    if (!currentActivity.state().equals(nextActivity.state())) {
                        log.debug("Hero {} activity changed: {} -> {}", h.getId(), currentActivity.state(), nextActivity.state());
                        log.debug("Queueing world event for hero {}", h.getId());
                        worldEventQueue.add(WorldEvent.from(nextActivity));
                    }
                }
        );
        log.debug("World tick completed");
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
            case IDLE, RESTING, DUNGEON -> HeroActivity.roaming(current.heroId(), areaId);
            case ROAMING -> HeroActivity.inEncounter(current.heroId(), current.areaId());
            case IN_ENCOUNTER -> HeroActivity.roaming(current.heroId(), current.areaId());
        };
    }
}
