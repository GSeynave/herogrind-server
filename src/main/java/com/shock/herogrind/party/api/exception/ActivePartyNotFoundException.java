package com.shock.herogrind.party.api.exception;

public class ActivePartyNotFoundException extends RuntimeException{

    public ActivePartyNotFoundException() {
        super("No Active PartyFound ");
    }
}
