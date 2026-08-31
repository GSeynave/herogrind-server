package com.shock.herogrind.party.internal.domain;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PartyRepository {

    List<Party> findAll();
    Optional<Party> findByAreaId(UUID id);
    Optional<Party> findActive();
    void save(Party party);
    void delete(UUID id);
    Optional<Party> findByHeroId(UUID heroId);
}
