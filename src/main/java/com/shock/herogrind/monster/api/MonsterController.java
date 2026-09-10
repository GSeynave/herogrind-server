package com.shock.herogrind.monster.api;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shock.herogrind.monster.internal.application.GetMonsterUseCase;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/v1/monsters")
@RequiredArgsConstructor
public class MonsterController {
  private final GetMonsterUseCase getMonsterUseCase;

  @GetMapping
  public ResponseEntity<List<MonsterInfo>> getMonsters() {
    List<MonsterInfo> monsters = getMonsterUseCase.getAllMonsters();
    return ResponseEntity.ok(monsters);
  }
}
