package com.shock.herogrind.party.api.exception;

import java.util.UUID;

public class AreaPartyNotFoundException extends RuntimeException{

    public AreaPartyNotFoundException(UUID areaId) {
        super("No Party Found in area : " + areaId);
    }
}
