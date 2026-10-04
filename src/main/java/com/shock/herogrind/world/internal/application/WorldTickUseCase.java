package com.shock.herogrind.world.internal.application;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

import org.springframework.stereotype.Component;

import com.shock.herogrind.hero.api.HeroFacade;
import com.shock.herogrind.world.internal.domain.HeroActivityRepository;
import com.shock.herogrind.world.internal.domain.HeroActivityState;
import com.shock.herogrind.world.internal.domain.WorldEvent;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class WorldTickUseCase {

  private final HeroFacade heroFacade;
  private final HeroActivityRepository heroActivityRepository;
  private final List<ActivityHandler> activityHandlers;

  private final Queue<WorldEvent> worldEventQueue = new ArrayDeque<>();

  public void execute() {
    log.debug("Starting world tick");
    var heroes = heroFacade.getAllHeroes();
    log.debug("Processing {} heroes", heroes.size());

    heroes.forEach(h -> {
      log.trace("Processing hero {}", h.getId());
      var currentActivity = heroActivityRepository.getOrIdle(h.getId());

      var result = activityHandler(currentActivity.state()).handle(currentActivity);

      result.getNextActivity().ifPresent(heroActivityRepository::save);

      result.getEvents().forEach(e -> worldEventQueue.add(e));
    });

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

  private ActivityHandler activityHandler(HeroActivityState state) {
    return activityHandlers.stream()
        .filter(h -> h.supports(state))
        .findFirst()
        .orElseThrow(() -> new IllegalArgumentException("No handler for state: " + state));
  }
}
