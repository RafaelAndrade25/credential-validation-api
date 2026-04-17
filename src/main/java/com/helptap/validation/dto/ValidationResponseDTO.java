package com.helptap.validation.dto;


public record ValidationResponseDTO(
        boolean valid,              // true se credencial é válida
        String credential,          // Credencial validada
        String uf,                  // Estado
        String type,                // Tipo de credencial
        String name,                // Nome do profissional
        String specialty,           // Especialidade/Patente/Categoria
        String status,              // Status da credencial
        String message              // Mensagem de erro/sucesso
) {
    // Construtor para credencial válida
    public static ValidationResponseDTO valid(
            String credential,
            String uf,
            String type,
            String name,
            String specialty,
            String status) {
        return new ValidationResponseDTO(
                true,
                credential,
                uf,
                type,
                name,
                specialty,
                status,
                "Credencial válida e ativa"
        );
    }

    // Construtor para credencial inválida
    public static ValidationResponseDTO invalid(
            String credential,
            String uf,
            String type,
            String message) {
        return new ValidationResponseDTO(
                false,
                credential,
                uf,
                type,
                null,
                null,
                null,
                message
        );
    }
}
