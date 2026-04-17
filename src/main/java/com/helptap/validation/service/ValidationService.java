package com.helptap.validation.service;


import com.helptap.validation.dto.ValidationRequestDTO;
import com.helptap.validation.dto.ValidationResponseDTO;
import com.helptap.validation.exception.InvalidCredentialException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ValidationService {

    private final CrmValidationService crmValidationService;
    private final CorenValidationService corenValidationService;
    private final FunctionalIdValidationService functionalIdValidationService;

    public ValidationResponseDTO validate(ValidationRequestDTO request) {
        log.info("Iniciando validação - Type: {} - Credential: {} - UF: {}",
                request.type(), request.credential(), request.uf());

        try {
            return switch (request.type().toUpperCase()) {
                case "CRM" -> crmValidationService.validate(request.credential(), request.uf());
                case "COREN" -> corenValidationService.validate(request.credential(), request.uf());
                case "POLICE", "FIREFIGHTER" ->
                        functionalIdValidationService.validate(request.credential(), request.type());
                default -> throw new InvalidCredentialException(
                        "Tipo de credencial inválido: " + request.type()
                );
            };
        } catch (InvalidCredentialException e) {
            log.warn("Validação falhou: {}", e.getMessage());
            return ValidationResponseDTO.invalid(
                    request.credential(),
                    request.uf(),
                    request.type(),
                    e.getMessage()
            );
        }
    }
}
