package com.shock.herogrind.hero.api.web;

import com.shock.herogrind.hero.api.web.dto.HeroDetailsDto;
import com.shock.herogrind.hero.api.web.dto.HeroUnlockedDTO;
import com.shock.herogrind.hero.api.web.dto.UnlockHeroDTO;
import com.shock.herogrind.hero.internal.application.create.UnlockHeroCommand;
import com.shock.herogrind.hero.internal.application.create.UnlockHeroUseCase;
import com.shock.herogrind.hero.internal.application.get.GetHeroQuery;
import com.shock.herogrind.hero.internal.application.get.GetHeroUseCase;
import com.shock.herogrind.hero.internal.application.get.GetHeroesUseCase;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@AllArgsConstructor
@RequestMapping("/heroes")
public class HeroController {
    GetHeroesUseCase getHeroesUseCase;
    GetHeroUseCase getHeroUseCase;
    UnlockHeroUseCase unlockHeroUseCase;

    @PostMapping
    ResponseEntity<HeroUnlockedDTO> unlockHero(@RequestBody UnlockHeroDTO dto) {
        var command = new UnlockHeroCommand(dto.name());
        var result = unlockHeroUseCase.execute(command);
        return ResponseEntity.status(HttpStatus.CREATED).body(HeroUnlockedDTO.from(result));
    }
    @GetMapping
    ResponseEntity<List<HeroDetailsDto>> getHeroes() {
        var heroes = getHeroesUseCase.execute();
        return ResponseEntity.ok().body(heroes.stream().map(HeroDetailsDto::from).toList());
    }
    @GetMapping("/{id}")
    ResponseEntity<HeroDetailsDto> getHero(@PathVariable UUID id) {

        var query = new GetHeroQuery(id);
        var hero = getHeroUseCase.execute(query);
        return ResponseEntity.ok(HeroDetailsDto.from(hero));
    }
}
