package health_api.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

    public record CreatePatientRequest(
        @NotBlank String firstName,
        @NotBlank String lastName,
        @NotBlank String personalNumber,
        @NotNull LocalDate dateOfBirth
    ){}
