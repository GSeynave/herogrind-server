package com.shock.herogrind.world.api.web;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shock.herogrind.world.api.web.dto.HeroActivityDto;
import com.shock.herogrind.world.api.web.dto.WorldStateDto;
import com.shock.herogrind.world.internal.application.GetHeroActivitiesUseCase;
import com.shock.herogrind.world.internal.application.GetWorldStateUseCase;
import com.shock.herogrind.world.internal.application.WorldTickUseCase;
import com.shock.herogrind.world.internal.domain.WorldEvent;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("v1/world")
@RequiredArgsConstructor
public class WorldController {

  private final GetHeroActivitiesUseCase getHeroActivitiesUseCase;
  private final WorldTickUseCase worldTickUseCase;
  private final GetWorldStateUseCase getWorldStateUseCase;

  @GetMapping("/heroes/activities")
  public ResponseEntity<List<HeroActivityDto>> getHeroActivities() {
    var activities = getHeroActivitiesUseCase.execute();
    return ResponseEntity.ok(
        activities.stream()
            .map(HeroActivityDto::from)
            .toList());
  }

  @GetMapping("/events")
  public ResponseEntity<List<WorldEvent>> getWorldTick() {
    var events = worldTickUseCase.getEvents();
    return ResponseEntity.ok(events);
  }

  @GetMapping("/state")
  public ResponseEntity<WorldStateDto> getWorldState() {
    var state = getWorldStateUseCase.execute();
    return ResponseEntity.ok(state);
  }
}
