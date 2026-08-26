package com.shock.herogrind.area.internal.application;

import com.shock.herogrind.area.internal.domain.AreaRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class GetAreasUseCase {
    private final AreaRepository areaRepository;

    public List<AreaView> execute(){
        var areas = areaRepository.findAll();

        return areas.stream()
                .map(AreaView::from)
                .toList();
    }
}
