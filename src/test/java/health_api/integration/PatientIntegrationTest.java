package health_api.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import health_api.model.Patient;
import health_api.repository.PatientRepository;
import jakarta.transaction.Transactional;

@SpringBootTest
@Transactional 
class PatientIntegrationTest {

	@Autowired
    private PatientRepository patientRepository;
	
	@Test
	void contextLoads() {
	}

	@Test
	void savePatient_canBeFoundById(){
		Patient patient = new Patient(
            "John",
            "Smith",
            "200000000000",
            LocalDate.of(2000, 1, 1)
        );
		patientRepository.save(patient);
		Optional<Patient> result = patientRepository.findById(patient.getId());

		assertTrue(result.isPresent());
	}

	@Test 
	void savePatient_persistsCorrectData(){
		Patient patient = new Patient(
            "John",
            "Smith",
            "200000000000",
            LocalDate.of(2000, 1, 1)
        );
		patientRepository.save(patient);
		Optional<Patient> result = patientRepository.findById(patient.getId());

		assertTrue(result.isPresent());

		assertEquals(patient.getFirstName(), result.get().getFirstName());
		assertEquals(patient.getLastName(), result.get().getLastName());
		assertEquals(patient.getPersonalNumber(), result.get().getPersonalNumber());
		assertEquals(patient.getDateOfBirth(), result.get().getDateOfBirth());
	}

	@Test
	void deletePatient_removesPatient(){
		Patient patient = new Patient(
            "John",
            "Smith",
            "200000000000",
            LocalDate.of(2000, 1, 1)
        );
		patientRepository.save(patient);
		patientRepository.deleteById(patient.getId());
		Optional<Patient> result = patientRepository.findById(patient.getId());
		assertTrue(result.isEmpty());
	}
}
