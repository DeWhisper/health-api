package health_api.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import health_api.model.Appointment;

public interface AppointmentRepository extends JpaRepository<Appointment, UUID>{
}