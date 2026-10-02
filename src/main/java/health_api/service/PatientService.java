package health_api.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import health_api.exception.PatientNotFoundException;
import health_api.model.Patient;
import health_api.repository.PatientRepository;
import health_api.dto.CreatePatientRequest;
import health_api.dto.UpdatePatientRequest;

@Service
public class PatientService {
    private final PatientRepository repo;

    // Constructor
    public PatientService(PatientRepository repo){
        this.repo = repo;
    }
    
    // Takes CreatePatientRequest Record and convert to patient
    public Patient createPatient(CreatePatientRequest request){
        Patient patient = new Patient(
            request.firstName(),
            request.lastName(),
            request.personalNumber(),
            request.dateOfBirth()
        );
        return repo.save(patient);
    }

    // Get Patient by id, throw custom exception if not found
    public Patient getPatientById(UUID id){
        return repo.findById(id)
            .orElseThrow(() -> new PatientNotFoundException("Patient not found with id: " + id));
    } 

    public List<Patient> getAllPatients(){
        return repo.findAll();
    }

    // Get Patient using id, update Patient using UpdatePatientRequest
    public Patient updatePatient(UUID id, UpdatePatientRequest request){
        Patient patient = repo.findById(id)
            .orElseThrow(() -> new PatientNotFoundException("Patient not found with id: " + id));
        
        patient.setFirstName(request.firstName());
        patient.setLastName(request.lastName());
        patient.setPersonalNumber(request.personalNumber());
        patient.setDateOfBirth(request.dateOfBirth());

        return repo.save(patient);
    }

    public void deletePatient(Patient patient){
        repo.delete(patient);
    }

    public void deletePatientById(UUID id){
        Patient patient = repo.findById(id)
            .orElseThrow(() -> new PatientNotFoundException(
                "Patient not found with id: " + id
            ));
        repo.delete(patient);
    }

}
