package com.shock.herogrind.world.internal.application;

import com.shock.herogrind.area.api.AreaFacade;
import com.shock.herogrind.hero.api.HeroFacade;
import com.shock.herogrind.party.api.PartyFacade;
import com.shock.herogrind.party.api.PartyInfo;
import com.shock.herogrind.world.internal.domain.HeroActivity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class WorldTickUseCase {

    private final HeroFacade heroFacade;
    private final AreaFacade areaFacade;
    private final PartyFacade partyFacade;

    private final Map<UUID, HeroActivity> heroActivityMap;

    public void execute() {
        System.out.println("New tick");
        var heroes = heroFacade.getAllHeroes();
        var parties = partyFacade.getPartyInfo();
        heroes.forEach(h -> {
                    var currentActivity = heroActivityMap.getOrDefault(
                            h.getId(),
                            HeroActivity.idle(h.getId())
                    );

                    var heroParty = parties.stream()
                            .filter(p -> p.members().contains(h.getId()))
                            .findFirst();

                    var nextActivity = resolveActivity(currentActivity, heroParty);
                    System.out.println(nextActivity.log());
                    heroActivityMap.put(h.getId(), nextActivity);
                }
        );
    }

    protected HeroActivity resolveActivity(HeroActivity current, Optional<PartyInfo> party) {
        if (party.isEmpty()) {
            return HeroActivity.idle(current.heroId());
        }

        var heroParty = party.get();

        if (heroParty.partyType().equals("ACTIVE")) {
            return HeroActivity.inDungeon(current.heroId());
        }

        return resolveAreaActivity(current, heroParty.areaId());
    }

    private HeroActivity resolveAreaActivity(HeroActivity current, UUID areaId) {
        return switch (current.state()) {
            case IDLE, RESTING, DUNGEON -> HeroActivity.roaming(current.heroId(), areaId);
            case ROAMING -> current;
            case IN_ENCOUNTER -> current;
        };
    }
}
