package com.condofacil.dto;

import com.condofacil.model.RoleType;
import com.condofacil.model.Users;

import java.util.UUID;

public record UserResponseDTO(
        UUID userUuid,
        String login,
        RoleType roleType,

        UUID personUuid,
        String fullName,
        String email,
        String phone
) {
    public static UserResponseDTO fromEntity(Users entity){
        if (entity == null) return null;

        UUID personUuid = (entity.getPerson() != null) ? entity.getPerson().getPersonUuid() : null;
        String name = (entity.getPerson() != null) ? entity.getPerson().getFullName() : null;
        String pEmail =  (entity.getPerson() != null) ? entity.getPerson().getEmail() : null;
        String pPhone = (entity.getPerson() != null) ? entity.getPerson().getPhone() : null;

        return new UserResponseDTO(
                entity.getUserUuid(),
                entity.getLogin(),
                entity.getRoleType(),
                personUuid,
                name,
                pEmail,
                pPhone
        );
    }
}
