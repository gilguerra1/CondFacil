package com.condofacil.controllers;

import com.condofacil.dto.UnitRequestDTO;
import com.condofacil.dto.UnitResponseDTO;
import com.condofacil.services.UnitService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/unit")
public class UnitController {

    @Autowired
    private UnitService service;

    @PostMapping
    public ResponseEntity<UnitResponseDTO> create(
            @RequestBody @Valid UnitRequestDTO requestDTO,
            UriComponentsBuilder uriComponentsBuilder
            )
    {
        UnitResponseDTO responseDTO = service.create(requestDTO);

        URI uri = uriComponentsBuilder.path("/unit/{unitUuid").buildAndExpand(responseDTO.uuid()).toUri();

        return ResponseEntity.created(uri).body(responseDTO);
    }

    @GetMapping("/{uuid}")
    public ResponseEntity<UnitResponseDTO> findByUuid(@PathVariable UUID uuid){

        UnitResponseDTO responseDTO = service.findByUuid(uuid);

        return ResponseEntity.ok(responseDTO);
    }
}
