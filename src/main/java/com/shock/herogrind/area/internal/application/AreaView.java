package com.shock.herogrind.area.internal.application;

import com.shock.herogrind.area.internal.domain.Area;

import java.util.UUID;

public record AreaView(UUID id, String name, Boolean isUnlocked) {

    public static AreaView from(Area area){
        return new AreaView(area.getId(), area.getName(), area.isUnlocked());
    }
}
