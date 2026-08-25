package com.shock.herogrind.party.api.web;

import com.shock.herogrind.party.api.web.dto.AddMemberToPartyBody;
import com.shock.herogrind.party.api.web.dto.PartyDTO;
import com.shock.herogrind.party.internal.application.AddMemberToActivePartyCommand;
import com.shock.herogrind.party.internal.application.AddMemberToActivePartyUseCase;
import com.shock.herogrind.party.internal.application.AddMemberToAreaPartyCommand;
import com.shock.herogrind.party.internal.application.AddMemberToAreaPartyUseCase;
import com.shock.herogrind.party.internal.application.RemoveMemberFromActivePartyCommand;
import com.shock.herogrind.party.internal.application.RemoveMemberFromActivePartyUseCase;
import com.shock.herogrind.party.internal.application.RemoveMemberFromAreaPartyCommand;
import com.shock.herogrind.party.internal.application.RemoveMemberFromAreaPartyUseCase;
import com.shock.herogrind.party.internal.application.get.GetActivePartyUseCase;
import com.shock.herogrind.party.internal.application.get.GetAreaPartyQuery;
import com.shock.herogrind.party.internal.application.get.GetAreaPartyUseCase;
import com.shock.herogrind.party.internal.application.get.GetPartiesUseCase;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/parties")
@AllArgsConstructor
public class PartyController {

    private final GetPartiesUseCase getPartiesUseCase;
    private final GetActivePartyUseCase getActivePartyUseCase;
    private final GetAreaPartyUseCase getAreaPartyUseCase;

    private final AddMemberToActivePartyUseCase addMemberToActivePartyUseCase;
    private final AddMemberToAreaPartyUseCase addMemberToAreaPartyUseCase;

    private final RemoveMemberFromActivePartyUseCase removeMemberFromActivePartyUseCase;
    private final RemoveMemberFromAreaPartyUseCase removeMemberFromAreaPartyUseCase;

    @GetMapping()
    public ResponseEntity<List<PartyDTO>> getParties() {
        return ResponseEntity.ok(
                getPartiesUseCase.execute().stream()
                        .map(PartyDTO::from)
                        .toList()
        );
    }

    @GetMapping(value = "/active")
    public ResponseEntity<PartyDTO> getActiveParty() {
        return ResponseEntity.ok(
                PartyDTO.from(getActivePartyUseCase.execute())
        );
    }

    @GetMapping(value = "/areas/{areaId}")
    public ResponseEntity<PartyDTO> getAreaParty(@PathVariable UUID areaId) {
        var query = new GetAreaPartyQuery(areaId);
        return ResponseEntity.ok(
                PartyDTO.from(getAreaPartyUseCase.execute(query))
        );
    }

    @PostMapping(value = "/active/members")
    public ResponseEntity<PartyDTO> addMemberToActiveParty(@RequestBody AddMemberToPartyBody body) {
        var command = new AddMemberToActivePartyCommand(body.memberId());
        return ResponseEntity.ok(
                PartyDTO.from(addMemberToActivePartyUseCase.execute(command))
        );
    }

    @PostMapping(value = "/areas/{areaId}/members")
    public ResponseEntity<PartyDTO> addMemberToAreaParty(@PathVariable UUID areaId, @Valid @RequestBody AddMemberToPartyBody body) {
        var command = new AddMemberToAreaPartyCommand(body.memberId(), areaId);
        return ResponseEntity.ok(
                PartyDTO.from(addMemberToAreaPartyUseCase.execute(command))
        );
    }

    @DeleteMapping(value = "/active/members/{memberId}")
    public ResponseEntity<Void> removeMemberFromActiveParty(@PathVariable UUID memberId) {
        var command = new RemoveMemberFromActivePartyCommand(memberId);
        removeMemberFromActivePartyUseCase.execute(command);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping(value = "/areas/{areaId}/members/{memberId}")
    public ResponseEntity<Void> removeMemberFromAreaParty(@PathVariable UUID areaId, @PathVariable UUID memberId) {
        var command = new RemoveMemberFromAreaPartyCommand(memberId, areaId);
        removeMemberFromAreaPartyUseCase.execute(command);
        return ResponseEntity.noContent().build();
    }
}
