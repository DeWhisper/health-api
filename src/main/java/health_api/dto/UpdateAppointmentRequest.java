package health_api.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

    public record UpdateAppointmentRequest(
        @NotNull LocalDate date,
        @NotBlank String reason
    ){};
