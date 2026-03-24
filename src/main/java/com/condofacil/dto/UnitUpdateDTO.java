package com.condofacil.dto;

import com.condofacil.model.UnitType;

public record UnitUpdateDTO(
        String number,
        Integer floor,
        UnitType type
) {
}
