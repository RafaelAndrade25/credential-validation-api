package com.helptap.validation.service;

import com.helptap.validation.dto.ValidationResponseDTO;
import com.helptap.validation.exception.InvalidCredentialException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class CrmValidationService {

    //Simulando banco de dados de CRM valido

    private static final Map<String, CrmData> VALID_CRMS = new HashMap<>();

    static {
        // Popula CRMs válidos (simulando banco do CRM)
        VALID_CRMS.put("CRM/SP 123456", new CrmData("Dra. Maria Santos", "Cardiologia", "ACTIVE"));
        VALID_CRMS.put("CRM/SP 654321", new CrmData("Dr. João Silva", "Pediatria", "ACTIVE"));
        VALID_CRMS.put("CRM/SP 111111", new CrmData("Dr. Pedro Costa", "Ortopedia", "ACTIVE"));
        VALID_CRMS.put("CRM/SP 222222", new CrmData("Dra. Ana Paula", "Neurologia", "ACTIVE"));
        VALID_CRMS.put("CRM/SP 333333", new CrmData("Dr. Carlos Eduardo", "Clínica Geral", "ACTIVE"));

        // CRMs com problemas (para testes de erro)
        VALID_CRMS.put("CRM/SP 999999", new CrmData("Dr. Sergio", "Cirurgia", "INACTIVE"));
        VALID_CRMS.put("CRM/SP 888888", new CrmData("Dr. Carlito", "Dermatologia", "SUSPENDED"));
    }

    public ValidationResponseDTO validate(String crm, String uf) {
        String key = "CRM/" + uf + " " + crm;
        log.info("Validando CRM: {}", key);

        CrmData data = VALID_CRMS.get(key);

        if (data == null) {
            throw new InvalidCredentialException("CRM " + key + " não encontrado");
        }

        if (!"ACTIVE".equals(data.status)) {
            throw new InvalidCredentialException(
                    "CRM " + key + " não está ativo. Status: " + data.status
            );
        }

        log.info("CRM validado com sucesso: {} - {}", key, data.name);

        return ValidationResponseDTO.valid(
                crm,
                uf,
                "CRM",
                data.name,
                data.specialty,
                data.status
        );
    }

    private record CrmData(String name, String specialty, String status) {}
}
