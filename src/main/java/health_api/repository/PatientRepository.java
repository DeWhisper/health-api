package health_api.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import health_api.model.Patient;

public interface PatientRepository extends JpaRepository<Patient, UUID>{
} 
