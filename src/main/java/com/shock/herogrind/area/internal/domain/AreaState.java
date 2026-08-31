package com.shock.herogrind.area.internal.domain;

public enum AreaState {

    LOCKED,
    AVAILABLE,
    UNLOCKED;

    public Boolean isUnlocked(){
        return this == AVAILABLE;
    }
}
