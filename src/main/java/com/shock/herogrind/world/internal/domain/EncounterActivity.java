package com.shock.herogrind.world.internal.domain;

import java.util.List;
import java.util.UUID;

import com.shock.herogrind.combat.api.CombatActionInfo;
import com.shock.herogrind.combat.api.EncounterInfo;
import com.shock.herogrind.combat.api.EncounterStatusInfo;

import lombok.Data;

@Data
public class EncounterActivity implements HeroActivityPayload {

  private UUID encounterId;
  private UUID heroId;
  private Double heroHealth;
  private UUID enemyId;
  private Double enemyHealth;
  private EncounterStatusInfo status;
  private List<CombatActionInfo> actions;
  private Long nextResolutionAt;

  public boolean isReadyForResolution() {
    return nextResolutionAt != null && System.currentTimeMillis() >= nextResolutionAt;

  }

  public static EncounterActivity fromEncounterInfo(EncounterInfo encounterInfo) {
    EncounterActivity activity = new EncounterActivity();
    activity.setEncounterId(encounterInfo.encounterId());
    activity.setHeroId(encounterInfo.heroId());
    activity.setHeroHealth(encounterInfo.heroHealth());
    activity.setEnemyId(encounterInfo.enemyId());
    activity.setEnemyHealth(encounterInfo.enemyHealth());
    activity.setStatus(encounterInfo.status());
    activity.setActions(encounterInfo.actions());
    activity.setNextResolutionAt(encounterInfo.nextResolutionAt());
    return activity;

  }
}
