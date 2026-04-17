package com.helptap.validation.service;

import com.helptap.validation.dto.ValidationResponseDTO;
import com.helptap.validation.exception.InvalidCredentialException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class FunctionalIdValidationService {

    // Simula banco de dados de IDs Funcionais válidos
    private static final Map<String, FunctionalIdData> VALID_IDS = new HashMap<>();

    static {
        // Policiais (simulando banco SSP)
        VALID_IDS.put("123.456-7", new FunctionalIdData(
                "Sgt. Carlos Oliveira", "POLICE", "SP", "Sargento", "SSP-SP - 1º Batalhão", "ACTIVE"
        ));
        VALID_IDS.put("891.011-1", new FunctionalIdData(
                "Cabo José Santos", "POLICE", "RJ", "Cabo", "SSP-RJ - 5º DP", "ACTIVE"
        ));
        VALID_IDS.put("213.141-5", new FunctionalIdData(
                "Soldado Paulo Lima", "POLICE", "MG", "Soldado", "SSP-MG - BPTUR", "ACTIVE"
        ));

        // Bombeiros (simulando banco Corpo de Bombeiros)
        VALID_IDS.put("161.718.1", new FunctionalIdData(
                "Cabo Pedro Costa", "FIREFIGHTER", "SP", "Cabo", "CBM-SP - 3º Grupamento", "ACTIVE"
        ));
        VALID_IDS.put("920.212-2", new FunctionalIdData(
                "Sgt. Ricardo Alves", "FIREFIGHTER", "RJ", "Sargento", "CBM-RJ - 10º GBM", "ACTIVE"
        ));
        VALID_IDS.put("232.425-2", new FunctionalIdData(
                "Soldado André Silva", "FIREFIGHTER", "MG", "Soldado", "CBM-MG - 2º BBM", "ACTIVE"
        ));

        // IDs com problemas
        VALID_IDS.put("627.282-9", new FunctionalIdData(
                "Policial Cancelado", "POLICE", "SP", "Soldado", "SSP-SP", "CANCELLED"
        ));
    }

    public ValidationResponseDTO validate(String functionalId, String type) {
        log.info("Validando ID Funcional: {} - Type: {}", functionalId, type);

        FunctionalIdData data = VALID_IDS.get(functionalId);

        if (data == null) {
            throw new InvalidCredentialException("ID Funcional " + functionalId + " não encontrado");
        }

        // Verifica se o tipo bate (POLICE ou FIREFIGHTER)
        if (!data.type.equals(type)) {
            throw new InvalidCredentialException(
                    "ID Funcional " + functionalId + " não é do tipo " + type +
                            ". Tipo correto: " + data.type
            );
        }

        if (!"ACTIVE".equals(data.status)) {
            throw new InvalidCredentialException(
                    "ID Funcional " + functionalId + " não está ativo. Status: " + data.status
            );
        }

        log.info("ID Funcional validado com sucesso: {} - {}", functionalId, data.name);

        return ValidationResponseDTO.valid(
                functionalId,
                data.uf,
                type,
                data.name,
                data.rank + " - " + data.unit,
                data.status
        );
    }

    private record FunctionalIdData(
            String name,
            String type,
            String uf,
            String rank,
            String unit,
            String status
    ) {}
}
