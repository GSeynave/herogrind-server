package com.shock.herogrind.area.api;

import com.shock.herogrind.area.api.dto.AreaDto;
import com.shock.herogrind.area.internal.application.GetAreaQuery;
import com.shock.herogrind.area.internal.application.GetAreaUseCase;
import com.shock.herogrind.area.internal.application.GetAreasUseCase;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/areas")
@AllArgsConstructor
public class AreaController {

    private final GetAreasUseCase getAreasUseCase;
    private final GetAreaUseCase getAreaUseCase;

    @GetMapping
    public ResponseEntity<List<AreaDto>> getAll() {
        return ResponseEntity.ok(
                getAreasUseCase.execute().stream()
                        .map(AreaDto::from)
                        .toList()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<AreaDto> getById(@PathVariable UUID id) {
        var query = new GetAreaQuery(id);
        return ResponseEntity.ok(
                AreaDto.from(getAreaUseCase.execute(query))
        );
    }
}
