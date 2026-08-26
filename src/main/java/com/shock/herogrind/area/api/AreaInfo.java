package com.shock.herogrind.area.api;

import com.shock.herogrind.area.internal.application.AreaView;

import java.util.UUID;

public record AreaInfo(UUID id, String name, Boolean isUnlocked) {

    public static AreaInfo from(AreaView area) {
        return new AreaInfo(area.id(), area.name(), area.isUnlocked());
    }
}
