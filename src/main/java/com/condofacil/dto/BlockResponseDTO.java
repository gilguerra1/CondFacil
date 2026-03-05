package com.condofacil.dto;


import com.condofacil.model.Block;

import java.util.UUID;

public record BlockResponseDTO(
        UUID uuid,
        String name,
        String condominiumName
) {

    public static BlockResponseDTO fromEntity(Block entity){

        if (entity == null) return null;

        String condName = (entity .getCondominium() != null)
                            ? entity.getCondominium().getName()
                            : "Condominio não encontrado!";

        return new BlockResponseDTO(
                entity.getBlockUuid(),
                entity.getName(),
                condName
        );
    }
}
