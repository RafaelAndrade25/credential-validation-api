package com.helptap.validation.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;


public record ValidationRequestDTO(

        @NotBlank(message = "Credential (CRM/COREN/ID Funcional) é obrigatório")
        String credential, // "123456" ou "POL12345-SSP-SP"

        @NotBlank(message = "UF é obrigatório")
        @Pattern(regexp = "^[A-Z]{2}$", message = "UF deve ter 2 letras maiúsculas (ex: SP, RJ)")
        String uf, // "SP", "RJ", etc

        @NotBlank(message = "Type é obrigatório")
        @Pattern(
                regexp = "^(CRM|COREN|POLICE|FIREFIGHTER)$",
                message = "Type deve ser: CRM, COREN, POLICE ou FIREFIGHTER"
        )
        String type // "CRM", "COREN", "POLICE", "FIREFIGHTER"
) {}
