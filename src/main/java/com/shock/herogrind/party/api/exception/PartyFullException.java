package com.shock.herogrind.party.api.exception;

import java.util.UUID;

public class PartyFullException extends RuntimeException{

    public PartyFullException(UUID partyId) {
        super("Party " + partyId + " is full.");
    }
}
