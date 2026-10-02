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

import health_api.dto.CreatePatientRequest;
import health_api.dto.UpdatePatientRequest;
import health_api.exception.PatientNotFoundException;
import health_api.model.Patient;
import health_api.repository.PatientRepository;

@ExtendWith(MockitoExtension.class)
public class PatientServiceTest {
    
    @Mock
    private PatientRepository patientRepository;

    @InjectMocks
    private PatientService patientService;

    @Test
    void getPatientById_returnsPatient_whenPatientExists() {
        // Arrange
        UUID id = UUID.randomUUID();

        Patient patient = new Patient(
            "John",
            "Smith",
            "200000000000",
            LocalDate.of(2000, 1, 1)
        );

        when(patientRepository.findById(id))
            .thenReturn(Optional.of(patient));

        // Act
        Patient result = patientService.getPatientById(id);

        // Assert
        assertEquals(patient, result);
        verify(patientRepository).findById(id);
    }

    @Test
    void getPatientById_throwsException_whenPatientDoesNotExist(){
        UUID id = UUID.randomUUID();

        when(patientRepository.findById(id))
            .thenReturn(Optional.empty());

        PatientNotFoundException exception = assertThrows(PatientNotFoundException.class, () -> 
            patientService.getPatientById(id));
        
        assertEquals("Patient not found with id: " + id, exception.getMessage());
    }

    @Test
    void createPatient_savesPatient(){
        CreatePatientRequest request = new CreatePatientRequest(
            "John",
            "Smith",
            "200000000000",
            LocalDate.of(2000, 1, 1)
        );

        Patient patient = new Patient(
            "John",
            "Smith",
            "200000000000",
            LocalDate.of(2000, 1, 1)
        );

        when(patientRepository.save(any(Patient.class))).thenReturn(patient);

        Patient result = patientService.createPatient(request);

        assertEquals(patient, result);
        verify(patientRepository).save(any(Patient.class));
    }

    @Test
    void updatePatient_updatesPatient(){
        UUID id = UUID.randomUUID();

        UpdatePatientRequest request = new UpdatePatientRequest(
            "Jane",
            "Doe",
            "199501011234",
            LocalDate.of(1995, 1, 1)
        );

        Patient patient = new Patient(
            "John",
            "Smith",
            "200000000000",
            LocalDate.of(2000, 1, 1)
        );

        when(patientRepository.findById(id)).thenReturn(Optional.of(patient));
        when(patientRepository.save(patient)).thenReturn(patient);

        Patient result = patientService.updatePatient(id, request);

        assertEquals("Jane", result.getFirstName());
        assertEquals("Doe", result.getLastName());
        
        verify(patientRepository).save(patient);
    }

    @Test
    void deletePatient_deletesPatient_whenPatientExists(){
        UUID id = UUID.randomUUID();

        Patient patient = new Patient(
            "John",
            "Smith",
            "200000000000",
            LocalDate.of(2000, 1, 1)
        );

        when(patientRepository.findById(id))
            .thenReturn(Optional.of(patient));

        patientService.deletePatientById(id);

        verify(patientRepository).delete(patient);
    }

    @Test
    void deletePatient_throwsException_patientDoesNotExist(){
        UUID id = UUID.randomUUID();

        when(patientRepository.findById(id))
            .thenReturn(Optional.empty());

        PatientNotFoundException exception = assertThrows(PatientNotFoundException.class, () -> {
            patientService.deletePatientById(id);
        });

        assertEquals("Patient not found with id: " + id, exception.getMessage());
    }
}
