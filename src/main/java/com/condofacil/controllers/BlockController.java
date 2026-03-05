package com.condofacil.controllers;

import com.condofacil.dto.BlockRequestDTO;
import com.condofacil.dto.BlockResponseDTO;
import com.condofacil.services.BlockServices;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/block")
public class BlockController {

    @Autowired
    private BlockServices services;

    @PostMapping
    public ResponseEntity<BlockResponseDTO> create(
            @RequestBody @Valid BlockRequestDTO requestDTO,
            UriComponentsBuilder uriComponentsBuilder
            )
    {

        BlockResponseDTO responseDTO = services.create(requestDTO);

        URI uri = uriComponentsBuilder.path("/block/{blockUuid}").buildAndExpand(responseDTO.uuid()).toUri();

        return ResponseEntity.created(uri).body(responseDTO);
    }

    @GetMapping
    public ResponseEntity<List<BlockResponseDTO>> findAll(){

        List<BlockResponseDTO> result = services.findAll();

        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/{uuid}")
    public ResponseEntity<Void> delete(@PathVariable UUID uuid){

        services.delete(uuid);

        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{uuid}")
    public ResponseEntity<BlockResponseDTO> update(
            @PathVariable UUID uuid,
            @RequestBody @Valid BlockRequestDTO dto) {

        BlockResponseDTO responseDTO = services.update(uuid, dto);

        return ResponseEntity.ok(responseDTO);
    }

}
