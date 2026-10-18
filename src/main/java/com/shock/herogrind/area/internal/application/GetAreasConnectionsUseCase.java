package com.shock.herogrind.area.internal.application;

import java.util.List;

import org.springframework.stereotype.Service;

import com.shock.herogrind.area.api.AreaConnectionInfo;
import com.shock.herogrind.area.internal.domain.AreaConnectionRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class GetAreasConnectionsUseCase {
  private final AreaConnectionRepository areaConnectionRepository;

  public List<AreaConnectionInfo> execute() {
    return areaConnectionRepository.getAllAreasConnections();
  }
}
