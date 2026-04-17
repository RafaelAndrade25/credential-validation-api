package com.helptap.validation.controller;

import com.helptap.validation.dto.ValidationRequestDTO;
import com.helptap.validation.dto.ValidationResponseDTO;
import com.helptap.validation.service.ValidationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/validation")
@RequiredArgsConstructor
@Slf4j
public class ValidationController {

    private final ValidationService validationService;

    @Value("${validation.api.key}")
    private String validApiKey;

    /**
     * Endpoint principal de validação de credenciais
     * Requer autenticação via API Key no header
     */
    @PostMapping("/validate")
    public ResponseEntity<ValidationResponseDTO> validateCredential(
            @RequestHeader(value = "X-API-Key") String apiKey,
            @Valid @RequestBody ValidationRequestDTO request) {

        // Valida API Key
        if (!validApiKey.equals(apiKey)) {
            log.warn("Tentativa de acesso com API Key inválida");
            return ResponseEntity.status(401).body(
                    ValidationResponseDTO.invalid(
                            request.credential(),
                            request.uf(),
                            request.type(),
                            "API Key inválida"
                    )
            );
        }

        log.info("Requisição de validação recebida - Type: {}", request.type());

        ValidationResponseDTO response = validationService.validate(request);

        return ResponseEntity.ok(response);
    }
    /**
     * Health check
     */
    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("Credential Validation API is running - No database required!");
    }
}
