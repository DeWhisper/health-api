package health_api.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import health_api.model.Appointment;
import health_api.model.Patient;
import health_api.repository.AppointmentRepository;
import health_api.repository.PatientRepository;

@SpringBootTest
@Transactional 
public class AppointmentIntegrationTest {

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private PatientRepository patientRepository;

	@Test
	void saveAppointment_canBeFoundById(){
		Patient patient = new Patient(
            "John",
            "Smith",
            "200000000000",
            LocalDate.of(2000, 1, 1)
        );

		patientRepository.save(patient);

        Appointment appointment = new Appointment(
            patient,
            LocalDate.of(2000, 1, 1),
            "Check-up"
        );

        appointmentRepository.save(appointment);

		Optional<Appointment> result = appointmentRepository.findById(appointment.getId());

		assertTrue(result.isPresent());
	}

	@Test 
	void saveAppointment_persistsCorrectData(){
		Patient patient = new Patient(
            "John",
            "Smith",
            "200000000000",
            LocalDate.of(2000, 1, 1)
        );

		patientRepository.save(patient);

        Appointment appointment = new Appointment(
            patient,
            LocalDate.of(2000, 1, 1),
            "Check-up"
        );

        appointmentRepository.save(appointment);

		Optional<Appointment> result = appointmentRepository.findById(appointment.getId());

		assertTrue(result.isPresent());

		assertEquals(appointment.getPatient().getId(), result.get().getPatient().getId());
		assertEquals(appointment.getDate(), result.get().getDate());
		assertEquals(appointment.getReason(), result.get().getReason());
	}

	@Test
	void deleteAppointment_removesAppointment(){
		Patient patient = new Patient(
            "John",
            "Smith",
            "200000000000",
            LocalDate.of(2000, 1, 1)
        );

		patientRepository.save(patient);
        
        Appointment appointment = new Appointment(
            patient,
            LocalDate.of(2000, 1, 1),
            "Check-up"
        );

        appointmentRepository.save(appointment);

        appointmentRepository.deleteById(appointment.getId());

		Optional<Appointment> result = appointmentRepository.findById(appointment.getId());

		assertTrue(result.isEmpty());
	}
}
