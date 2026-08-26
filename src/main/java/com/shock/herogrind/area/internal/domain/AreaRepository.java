package com.shock.herogrind.area.internal.domain;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AreaRepository {

    List<Area> findAll();

    Optional<Area> findById(UUID id);
}
