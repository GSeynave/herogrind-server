# Roaming system

## Purpose

The world simulation represents every in-world hero and monster as a `WorldActor`.
Actors roam within their area, acquire opposing actors in aggro range, pursue them,
attack at their own range, and can fight many-to-many.

The simulation is server-authoritative and currently advances once per second.
World state remains in memory; persistence and WebSockets are future work.

## Actor model

`WorldActor` is runtime simulation state owned by the world module. It contains:

- identity and type: `HERO` or `MONSTER`
- area-local normalized position (`x` and `y` in `[0, 1]`)
- roaming destination and behavior
- current target
- health, damage, attack range, aggro range, movement speed, and attack cooldown

A hero remains a `HERO` actor through its full lifecycle. Ghosts are heroes whose
actor behavior is a ghost behavior; they are not a separate actor type.

Main behaviors are:

- `ROAMING`
- `PURSUING`
- `ATTACKING`
- `DYING` / `DEAD`
- `GHOST_WAITING`, `GHOST_TRAVELING`, `GHOST_RESURRECTING`

Town-idle heroes are synchronized as roaming actors. After resurrection, a ghost
actor is restored to full health and resumes roaming in the resurrection area.

## Tick flow

`WorldTickUseCase` performs these steps:

1. Synchronize hero actors from hero activities.
2. Synchronize monster actors from seeded monster entities.
3. Run one behavior handler per actor against a stable actor snapshot.
4. Resolve all eligible attacks together.
5. Move defeated heroes into the existing death/ghost activity lifecycle.
6. Publish combat and lifecycle events.
7. Continue legacy activity handling where it still owns the lifecycle.

Roaming hero activities deliberately no longer invoke `RoamingActivityHandler`.
That prevents the old one-to-one encounter system from creating new combat in
parallel with actor combat.

## Behavior and combat rules

`WorldActor` owns the core business predicates:

- `canAggro(candidate)`: opposing, living/non-ghost actor in the same area and
  within aggro range.
- `canPursue(candidate)`: opposing eligible actor in the same area.
- `canAttack(candidate)`: opposing eligible actor in the same area and within
  attack range.

Roaming actors randomly choose one eligible target. If none exists, they move
toward a randomly selected point in their area. Pursuing actors move toward their
selected target until their own attack range is reached. Invalid, dead, or
unavailable targets return an actor to roaming.

When an actor is already being attacked within its own attack range, it locks
onto the incoming attacker immediately. This prevents a roaming target from
walking away from an active melee exchange. Initial aggro selection remains
random, and multiple actors may still attack the same target.

Attack resolution aggregates all damage directed at each target in the tick.
This permits, for example, two heroes attacking one monster and two monsters
attacking one hero in the same tick. Hero attacks have a two-second cooldown;
monster attacks have a three-second cooldown.

## Monster actors

The data seeder creates three monster entities in each PvE area. A monster entity
is synchronized to exactly one `MONSTER` actor, with an independent random spawn
position and slower movement than a hero. Dead monster actors remain dead;
respawn and configurable per-area populations are intentionally deferred.

## HTTP world-state contract

`GET /v1/world/state` now includes `actors` in addition to the legacy fields.

```json
{
  "actors": [
    {
      "id": "uuid",
      "type": "HERO",
      "areaId": "uuid",
      "positionX": 0.42,
      "positionY": 0.73,
      "behavior": "PURSUING",
      "targetId": "uuid",
      "health": 42.0,
      "maxHealth": 50.0
    }
  ]
}
```

The legacy `heroActivities`, `encounters`, and `ghosts` fields remain available
during the migration.

## Client integration

The client polls world state every second. When `actors` is present, actor
positions are mapped from normalized area coordinates to canvas coordinates and
take precedence over legacy hero-activity positions. Ghost travel remains
positioned from the ghost lifecycle data.

The canvas uses a 60-pixel world-grid cell, making each 10-by-8 area render at
600 by 480 pixels. Between polls, each actor position is interpolated over one
second from the previously displayed position to the latest server-authoritative
position. This makes roaming and pursuit appear continuous without giving the
client authority over simulation state.

Monsters are rendered from `MONSTER` actors at their actual positions and with
their current actor health. Hero health is also rendered from the actor snapshot.
Monster names still come from the monster catalogue, matched by actor ID.

## Key classes

Server:

- `WorldActor`, `WorldActorBehavior`, `WorldSpatialRules`
- `ActorTickUseCase` and `ActorBehaviorHandler`
- `RoamingActorBehaviorHandler`, `PursuingActorBehaviorHandler`,
  `AttackingActorBehaviorHandler`
- `CombatResolutionUseCase`
- `SynchronizeWorldActorsUseCase`, `SynchronizeMonsterActorsUseCase`
- `HeroActorDeathTransitionUseCase`
- `GetWorldStateUseCase`

Client:

- `model/world/WorldActorDto.ts`
- `world/actorUtils.ts`
- `rendering/heroRenderer.ts`
- `rendering/monsterRenderer.ts`

## Deferred work

- Replace in-memory actor storage with database persistence.
- Replace polling with WebSocket state/event delivery.
- Configure monster population, respawn, and scaling per area.
- Fully retire the remaining legacy encounter implementation after migration.
- Add party-level actor behavior.
