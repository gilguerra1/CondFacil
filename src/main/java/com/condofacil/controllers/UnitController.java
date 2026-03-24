package com.condofacil.controllers;

import com.condofacil.dto.UnitRequestDTO;
import com.condofacil.dto.UnitResponseDTO;
import com.condofacil.dto.UnitUpdateDTO;
import com.condofacil.services.UnitService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
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

        URI uri = uriComponentsBuilder.path("/unit/{uuid}").buildAndExpand(responseDTO.uuid()).toUri();

        return ResponseEntity.created(uri).body(responseDTO);
    }

    @GetMapping("/{uuid}")
    public ResponseEntity<UnitResponseDTO> findByUuid(@PathVariable UUID uuid){

        UnitResponseDTO responseDTO = service.findByUuid(uuid);

        return ResponseEntity.ok(responseDTO);
    }

    @GetMapping
    public  ResponseEntity<List<UnitResponseDTO>> findAll(){

        List<UnitResponseDTO> result = service.findAll();

        return ResponseEntity.ok(result);
    }

    @PutMapping("/{uuid}")
    public ResponseEntity<UnitResponseDTO> update(
            @PathVariable UUID uuid,
            @RequestBody @Valid  UnitUpdateDTO dto){

        UnitResponseDTO responseDTO = service.update(uuid, dto);

        return  ResponseEntity.ok(responseDTO);
    }

    @DeleteMapping("/{uuid}")
    public ResponseEntity<Void> delete(@PathVariable UUID uuid){

        service.delete(uuid);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/block/{uuid}")
    public ResponseEntity<List<UnitResponseDTO>> findByBlockUuid(@PathVariable UUID uuid){
        return ResponseEntity.ok(service.findUnitsByBlockUuid(uuid));
    }

}
