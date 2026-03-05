package com.condofacil.controllers;

import com.condofacil.dto.CondominiumFilterDTO;
import com.condofacil.dto.CondominiumRequestDTO;
import com.condofacil.dto.CondominiumResponseDTO;
import com.condofacil.services.CondominiumServices;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/condominium")
public class CondominiumController {

    @Autowired
    private CondominiumServices service;

    @PostMapping
    public ResponseEntity<CondominiumResponseDTO> create(
            @RequestBody @Valid CondominiumRequestDTO request,
            UriComponentsBuilder uriBuilder) {

        CondominiumResponseDTO response = service.create(request);

        URI uri = uriBuilder.path("/condominium/{uuid}").buildAndExpand(response.uuid()).toUri();

        return ResponseEntity.created(uri).body(response);
    }

    @GetMapping
    public ResponseEntity<List<CondominiumResponseDTO>> search(
            CondominiumFilterDTO filter
    ){
        List<CondominiumResponseDTO> result = service.findAll(filter);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/search")
    public ResponseEntity<List<CondominiumResponseDTO>> findByNameContaining(@RequestParam String name){

        List<CondominiumResponseDTO> result = service.findByNameContaining(name);

        return ResponseEntity.ok(result);
    }

    @GetMapping("/{uuid}")
    public ResponseEntity<CondominiumResponseDTO> getByUuid(@PathVariable UUID uuid) {

        CondominiumResponseDTO responseDTO = service.findByUuid(uuid);

        return ResponseEntity.ok(responseDTO);
    }

    @DeleteMapping("/{uuid}")
    public ResponseEntity<Void> delete(@PathVariable UUID uuid){

        service.delete(uuid);

        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{uuid}")
    public ResponseEntity<CondominiumResponseDTO> update(
            @PathVariable UUID uuid,
            @RequestBody @Valid CondominiumRequestDTO dto) {

        CondominiumResponseDTO responseDTO = service.update(uuid, dto);

        return  ResponseEntity.ok(responseDTO);
    }


}
