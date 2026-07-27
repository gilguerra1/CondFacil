package com.condofacil.controllers;

import com.condofacil.dto.UnitResponseDTO;
import com.condofacil.dto.UserRequestDTO;
import com.condofacil.dto.UserResponseDTO;
import com.condofacil.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService service;

    @PostMapping
    public ResponseEntity<UserResponseDTO> create(
            @RequestBody @Valid UserRequestDTO requestDTO,
            UriComponentsBuilder uriComponentsBuilder
    )
    {
        UserResponseDTO responseDTO = service.create(requestDTO);

        URI uri = uriComponentsBuilder.path("/user/{uuid}").buildAndExpand(responseDTO.userUuid()).toUri();

        return ResponseEntity.created(uri).body(responseDTO);
    }

    @GetMapping
    public ResponseEntity<List<UserResponseDTO>> findAll(){

        List<UserResponseDTO> result = service.findAll();

        return ResponseEntity.ok(result);
    }
}
