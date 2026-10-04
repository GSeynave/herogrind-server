package com.shock.herogrind.world.internal.application;

import java.util.concurrent.TimeUnit;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class WorldTickScheduler {
  private final WorldTickUseCase worldTickUseCase;

  @Scheduled(fixedRate = 1, timeUnit = TimeUnit.SECONDS)
  public void execute() {
    worldTickUseCase.execute();
  }
}
