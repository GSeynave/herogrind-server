package com.shock.herogrind.monster.api.web;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shock.herogrind.monster.internal.application.GetMonsterUseCase;
import com.shock.herogrind.monster.api.web.dto.MonsterDto;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/v1/monsters")
@RequiredArgsConstructor
public class MonsterController {
  private final GetMonsterUseCase getMonsterUseCase;

  @GetMapping
  public ResponseEntity<List<MonsterDto>> getMonsters() {
    List<MonsterDto> monsters = getMonsterUseCase.getAllMonsters().stream()
        .map(MonsterDto::from)
        .toList();
    return ResponseEntity.ok(monsters);
  }
}
