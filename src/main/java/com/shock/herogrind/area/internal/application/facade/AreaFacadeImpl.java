package com.shock.herogrind.area.internal.application.facade;

import com.shock.herogrind.area.api.AreaFacade;
import com.shock.herogrind.area.api.AreaInfo;
import com.shock.herogrind.area.internal.application.GetAreaQuery;
import com.shock.herogrind.area.internal.application.GetAreaUseCase;
import com.shock.herogrind.area.internal.application.GetAreasUseCase;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
@AllArgsConstructor
public class AreaFacadeImpl implements AreaFacade {

    private final GetAreasUseCase getAreasUseCase;
    private final GetAreaUseCase getAreaUseCase;


    @Override
    public List<AreaInfo> getAreas() {
        var areas = getAreasUseCase.execute();
        return areas.stream().map(AreaInfo::from).toList();
    }

    @Override
    public AreaInfo getAreaById(UUID areaId) {
        var query = new GetAreaQuery(areaId);
        var area = getAreaUseCase.execute(query);
        return AreaInfo.from(area);
    }
}
