package com.condofacil.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record BlockRequestDTO(

        @NotBlank(message = "O nome é obrigatório")
        String name,

        @NotNull(message = "O UUID do condomínio é obrigatório")
        UUID condominiumUuid
) {}
