package com.shock.herogrind.world.internal.application;

import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
@AllArgsConstructor
public class WorldTickScheduler {
    private final WorldTickUseCase worldTickUseCase;

    @Scheduled(fixedRate = 1, timeUnit = TimeUnit.SECONDS)
    public void execute() {
        worldTickUseCase.execute();
    }
}
