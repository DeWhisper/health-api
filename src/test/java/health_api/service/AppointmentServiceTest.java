package health_api.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import health_api.dto.CreateAppointmentRequest;
import health_api.dto.UpdateAppointmentRequest;
import health_api.exception.AppointmentNotFoundException;
import health_api.model.Appointment;
import health_api.model.Patient;
import health_api.repository.AppointmentRepository;
import health_api.repository.PatientRepository;

@ExtendWith(MockitoExtension.class)
public class AppointmentServiceTest {
    
    @Mock 
    private AppointmentRepository appointmentRepository;
    @Mock
    private PatientRepository patientRepository;

    @InjectMocks
    private AppointmentService appointmentService;

    @Test
    void getAppointmentById_returnsAppointment_whenAppointmentExist(){
        Patient patient = new Patient(
            "John",
            "Smith",
            "200000000000",
            LocalDate.of(2000, 1, 1)
        );

        // Appointment Id
        UUID id = UUID.randomUUID();

        Appointment appointment = new Appointment(
            patient,
            LocalDate.of(2020,10,10),
            "Sick"
        );

        when(appointmentRepository.findById(id))
            .thenReturn(Optional.of(appointment));

        Appointment result = appointmentService.getAppointmentById(id);

        assertEquals(appointment, result);
        verify(appointmentRepository).findById(id);
    }

    @Test
    void getAppointmentById_throwsException_whenAppointmentDoesNotExist(){
        UUID id = UUID.randomUUID();

        when(appointmentRepository.findById(id))
            .thenReturn(Optional.empty());

        AppointmentNotFoundException exception =
            assertThrows(AppointmentNotFoundException.class, () -> appointmentService.getAppointmentById(id));
        
        assertEquals("Appointment not found with id: " + id, exception.getMessage());
    }

    @Test
    void createAppointment_savesAppointment(){
        // Patient to have an appointment
        Patient patient = new Patient(
            "John",
            "Smith",
            "200000000000",
            LocalDate.of(2000, 1, 1)
        );

        // What we expect
        Appointment appointment = new Appointment(
            patient,
            LocalDate.of(2020,10,10),
            "Sick"
        );

        // What we send in
        CreateAppointmentRequest request = new CreateAppointmentRequest(
            patient.getId(),
            LocalDate.of(2020, 10, 10),
            "Sick"
        );

        when(appointmentRepository.save(any(Appointment.class))).thenReturn(appointment);
        when(patientRepository.findById(patient.getId())).thenReturn(Optional.of(patient));

        Appointment result = appointmentService.createAppointment(request);

        assertEquals(appointment, result);
        verify(appointmentRepository).save(any(Appointment.class));
    }

    @Test
    void updateAppointment_updatesAppointment(){
        UUID id = UUID.randomUUID();
        Patient patient = new Patient(
            "John",
            "Smith",
            "200000000000",
            LocalDate.of(2000, 1, 1)
        );
        Appointment appointment = new Appointment(
            patient,
            LocalDate.of(2020,10,10),
            "Checkup"
        );
        UpdateAppointmentRequest request = new UpdateAppointmentRequest(
            LocalDate.of(2020, 10, 10),
            "Sick"
        );

        when(appointmentRepository.findById(id)).thenReturn(Optional.of(appointment));
        when(appointmentRepository.save(appointment)).thenReturn(appointment);

        Appointment result = appointmentService.updateAppointment(id, request);

        assertEquals("Sick", result.getReason());
        assertEquals(LocalDate.of(2020, 10, 10), result.getDate());

        verify(appointmentRepository).save(appointment);
    }

    @Test
    void deleteAppointment_deletesAppointment_whenAppointmentExist(){
        UUID id = UUID.randomUUID();
        Patient patient = new Patient(
            "John",
            "Smith",
            "200000000000",
            LocalDate.of(2000, 1, 1)
        );
        Appointment appointment = new Appointment(
            patient,
            LocalDate.of(2020,10,10),
            "Checkup"
        );

        when(appointmentRepository.findById(id))
            .thenReturn(Optional.of(appointment));

        appointmentService.deleteAppointmentById(id);

        verify(appointmentRepository).delete(appointment);
    }

    @Test
    void deleteAppointment_throwsException_whenAppointmentDoesNotExist(){
        UUID id = UUID.randomUUID();

        when(appointmentRepository.findById(id))
            .thenReturn(Optional.empty());

        AppointmentNotFoundException exception 
            = assertThrows(AppointmentNotFoundException.class,
            () ->  appointmentService.deleteAppointmentById(id));

        assertEquals("Appointment not found with id: " + id, exception.getMessage());
    }
}
