package com.shock.herogrind.area.internal.application;

import com.shock.herogrind.area.api.exception.AreaNotFoundException;
import com.shock.herogrind.area.internal.domain.AreaRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class GetAreaUseCase {
    private final AreaRepository areaRepository;

    public AreaView execute(GetAreaQuery query) {
        var area = areaRepository.findById(query.id())
                .orElseThrow(() -> new AreaNotFoundException(query.id()));

        return AreaView.from(area);
    }
}
