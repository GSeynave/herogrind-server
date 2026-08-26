package com.shock.herogrind.area.api.dto;

import com.shock.herogrind.area.internal.application.AreaView;

import java.util.UUID;

public record AreaDto(UUID id, String name) {

    public static AreaDto from(AreaView view){
        return new AreaDto(view.id(), view.name());
    }

}
