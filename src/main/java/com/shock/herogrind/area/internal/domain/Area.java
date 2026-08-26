package com.shock.herogrind.area.internal.domain;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class Area {

    private UUID id;
    private String name;
    private Boolean unlocked;

    public Boolean isUnlocked(){
        return this.unlocked;
    }
}
