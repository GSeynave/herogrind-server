package com.shock.herogrind.world.internal.application;

import java.util.List;

import org.springframework.stereotype.Service;

import com.shock.herogrind.world.internal.domain.HeroActivityRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GetHeroActivitiesUseCase {
  private final HeroActivityRepository heroActivityRepository;
  private final HeroActivityViewAssembler heroActivityViewAssembler;

  public List<HeroActivityView> execute() {
    return heroActivityRepository.findAll().stream()
        .map(heroActivityViewAssembler::assemble)
        .toList();
  }
}
