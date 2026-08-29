package com.shock.herogrind.world.api.web;

import com.shock.herogrind.world.api.web.dto.HeroActivityDto;
import com.shock.herogrind.world.internal.application.GetHeroActivitiesUseCase;
import com.shock.herogrind.world.internal.application.WorldTickUseCase;
import com.shock.herogrind.world.internal.domain.WorldEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/world")
@RequiredArgsConstructor
public class WorldController {

    private final GetHeroActivitiesUseCase getHeroActivitiesUseCase;
    private final WorldTickUseCase worldTickUseCase;

    @GetMapping("/heroes/activities")
    public ResponseEntity<List<HeroActivityDto>> getHeroActivities() {
        var activities = getHeroActivitiesUseCase.execute();
        return ResponseEntity.ok(
                activities.stream()
                        .map(HeroActivityDto::from)
                        .toList()
        );
    }

    @GetMapping("/events")
    public ResponseEntity<List<WorldEvent>> getWorldTick(){
        var events = worldTickUseCase.getEvents();
        return ResponseEntity.ok( events
        );
    }

}
