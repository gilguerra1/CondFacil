package com.condofacil.dto;

import com.condofacil.model.Condominium;
import java.util.UUID;

public record CondominiumResponseDTO(
        UUID uuid,
        String name,
        String legalName,
        String taxId,
        String city,
        String state,
        String address,
        String managementEmail
) {

    public static CondominiumResponseDTO fromEntity(Condominium entity) {
        return new CondominiumResponseDTO(
                entity.getCondominiumUuid(),
                entity.getName(),
                entity.getLegalName(),
                entity.getTaxId(),
                entity.getCity(),
                entity.getState(),
                entity.getAddress(),
                entity.getManagementEmail()
        );
    }
}