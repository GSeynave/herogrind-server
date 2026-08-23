package com.shock.herogrind.hero.api.exception;

import java.util.UUID;

public class HeroNotFoundException extends RuntimeException{

    public HeroNotFoundException(UUID heroId) {
        super("Hero not found: " + heroId);
    }
}
