package com.shock.herogrind.party.api.exception;

import java.util.UUID;

public class HeroAlreadyInPartyException extends RuntimeException{

    public HeroAlreadyInPartyException(UUID heroId) {
        super("Hero " + heroId + " is already in the party.");
    }
}
