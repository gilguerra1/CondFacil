package com.condofacil.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record CondominiumRequestDTO(

        @NotBlank(message = "O nome é obrigatório")
        @Size(min = 3, message = "O nome deve ter no mínimo 3 caracteres")
        String name,

        @NotBlank(message = "A Razão Social é obrigatória")
        String legalName,

        @NotBlank(message = "O CNPJ é obrigatório")
        @Pattern(regexp = "\\d{2}\\.\\d{3}\\.\\d{3}/\\d{4}-\\d{2}", message = "CNPJ inválido (use o formato XX.XXX.XXX/0001-XX)")
        String taxId,

        @NotBlank String address,
        @NotBlank String number,
        @NotBlank String neighborhood,
        @NotBlank String city,
        @NotBlank String state,
        @NotBlank String zipCode,

        @Size(min = 10, max = 15)
        String conciergePhone,

        @Email(message = "Email inválido")
        String managementEmail
) {}