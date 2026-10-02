package health_api.dto;

import java.time.LocalDate;
import java.util.UUID;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateAppointmentRequest(
    @NotNull UUID patientId,
    @NotNull LocalDate date,
    @NotBlank String reason
) {}
