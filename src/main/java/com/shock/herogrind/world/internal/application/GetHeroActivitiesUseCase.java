package com.shock.herogrind.world.internal.application;

import com.shock.herogrind.world.internal.domain.HeroActivityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GetHeroActivitiesUseCase {
    private final HeroActivityRepository heroActivityRepository;

    public List<HeroActivityView> execute() {
        return heroActivityRepository.findAll().stream()
                .map(HeroActivityView::from)
                .toList();
    }
}
