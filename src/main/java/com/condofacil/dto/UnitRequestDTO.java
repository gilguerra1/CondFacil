package com.condofacil.dto;

import com.condofacil.model.UnitType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record UnitRequestDTO(
        @NotBlank String number,
        @NotNull Integer floor,
        @NotNull UnitType type,
        @NotNull UUID blockUuid
        ) {}
