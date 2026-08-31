package com.shock.herogrind.party.internal.infrastructure.persistence;

import com.shock.herogrind.party.internal.domain.Party;
import com.shock.herogrind.party.internal.domain.PartyRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

@Repository
public class InMemoryPartyRepository implements PartyRepository {

    Map<UUID, Party> parties = new HashMap<>();

    @Override
    public List<Party> findAll() {
        return new ArrayList<>(parties.values());
    }

    @Override
    public Optional<Party> findByAreaId(UUID partyId) {
        return Optional.ofNullable(parties.get(partyId));
    }

    @Override
    public void save(Party party) {
        parties.put(party.getAssignment().areaId(), party);
    }

    @Override
    public Optional<Party> findActive() {
        return Optional.ofNullable(parties.get(null));
    }

    @Override
    public void delete(UUID id) {
        AtomicReference<UUID> keyToDelete = new AtomicReference<>(UUID.randomUUID());
        parties.forEach((k, v) -> {
            if (v.getId().equals(id))
                keyToDelete.set(k);
        });
        parties.remove(keyToDelete.get());
    }

    @Override
    public Optional<Party> findByHeroId(UUID heroId) {
        AtomicReference<Party> party = new AtomicReference<>();
        parties.forEach((k, v) -> {
                    if (v.getMembers().contains(heroId)) party.set(v);
                }
        );
        return Optional.ofNullable(party.get());
    }
}
