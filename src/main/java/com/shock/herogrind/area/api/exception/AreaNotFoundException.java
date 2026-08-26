package com.shock.herogrind.area.api.exception;

import java.util.UUID;

public class AreaNotFoundException extends RuntimeException{
    public AreaNotFoundException(UUID id) {
        super("Area " + id + " not found.");
    }
}
