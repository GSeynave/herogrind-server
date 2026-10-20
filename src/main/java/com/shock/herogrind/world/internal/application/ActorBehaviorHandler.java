package com.shock.herogrind.world.internal.application;

import java.util.List;

import com.shock.herogrind.world.internal.domain.WorldActor;
import com.shock.herogrind.world.internal.domain.WorldActorBehavior;

public interface ActorBehaviorHandler {

  boolean supports(WorldActorBehavior behavior);

  WorldActor handle(WorldActor actor, List<WorldActor> actorSnapshot);
}
