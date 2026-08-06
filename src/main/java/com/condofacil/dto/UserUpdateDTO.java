package com.condofacil.dto;

import com.condofacil.model.RoleType;

public record UserUpdateDTO(
        String login,
        String password,
        RoleType roleType
) {

}
