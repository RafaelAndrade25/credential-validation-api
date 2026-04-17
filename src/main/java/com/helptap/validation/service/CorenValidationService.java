package com.helptap.validation.service;

import com.helptap.validation.dto.ValidationResponseDTO;
import com.helptap.validation.exception.InvalidCredentialException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class CorenValidationService {

    private static final Map<String, CorenData> VALID_CORENS = new HashMap<>();

    static {
        // Popula CORENs válidos (simulando banco do COREN)
        VALID_CORENS.put("Coren-SP 123.456-ENF", new CorenData("Ana Paula Ferreira", "Enfermeira", "Emergência", "ACTIVE"));
        VALID_CORENS.put("Coren-SP 654.321-ENF", new CorenData("Juliana Costa", "Técnica em Enfermagem", "UTI", "ACTIVE"));
        VALID_CORENS.put("Coren-SP 111.111-ENF", new CorenData("Fernanda Silva", "Enfermeira", "Pediatria", "ACTIVE"));
        VALID_CORENS.put("Coren-SP 222.222-ENF", new CorenData("Mariana Santos", "Auxiliar de Enfermagem", "Cirurgia", "ACTIVE"));
        VALID_CORENS.put("Coren-SP 333.333-ENF", new CorenData("Camila Oliveira", "Enfermeira", "SAMU", "ACTIVE"));

        // CORENs com problemas
        VALID_CORENS.put("Coren-SP 999.999-ENF", new CorenData("Fabiana", "Enfermeira", "Geral", "SUSPENDED"));
    }

    public ValidationResponseDTO validate(String coren, String uf) {
        String key = coren + "-" + uf;
        log.info("Validando COREN: {}", key);

        CorenData data = VALID_CORENS.get(key);

        if (data == null) {
            throw new InvalidCredentialException("COREN " + key + " não encontrado");
        }

        if (!"ACTIVE".equals(data.status)) {
            throw new InvalidCredentialException(
                    "COREN " + key + " não está ativo. Status: " + data.status
            );
        }

        log.info("COREN validado com sucesso: {} - {}", key, data.name);

        return ValidationResponseDTO.valid(
                coren,
                uf,
                "COREN",
                data.name,
                data.category + " - " + data.specialty,
                data.status
        );
    }

    private record CorenData(String name, String category, String specialty, String status) {}
}
