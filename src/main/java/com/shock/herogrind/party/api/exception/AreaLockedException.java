package com.shock.herogrind.party.api.exception;

import java.util.UUID;

public class AreaLockedException extends RuntimeException{

    public AreaLockedException(UUID areaId) {
        super("Can't add hero to party, area " + areaId + " is still locked.");
    }
}
