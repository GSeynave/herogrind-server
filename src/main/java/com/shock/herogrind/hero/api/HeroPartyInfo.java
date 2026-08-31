package com.shock.herogrind.hero.api;

import com.shock.herogrind.hero.internal.domain.HeroRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
public class HeroPartyInfo {
    private UUID id;
    private String name;
    private HeroRole role;
    private Integer level;


}
