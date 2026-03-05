package com.condofacil.dto;

import com.condofacil.model.Unit;
import com.condofacil.model.UnitType;

import java.util.UUID;

public record UnitResponseDTO(
        UUID uuid,
        String number,
        Integer floor,
        UnitType type,
        String blockName,
        String condominiumName,
        UUID blockUuid
        ) {

    public static UnitResponseDTO fromEntity(Unit entity){

        if (entity == null) return null;

        return new UnitResponseDTO(
                entity.getUnitUuid(),
                entity.getNumber(),
                entity.getFloor(),
                entity.getType(),
                entity.getBlock() != null ? entity.getBlock().getName():null,
                (entity.getBlock() != null && entity.getBlock().getCondominium() != null)
                    ? entity.getBlock().getCondominium().getName() : null,
                entity.getBlock() != null ? entity.getBlock().getBlockUuid() : null
        );
    }
}
