package health_api.controller;

import java.util.UUID;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import health_api.dto.CreatePatientRequest;
import health_api.dto.UpdatePatientRequest;
import health_api.model.Patient;
import health_api.service.PatientService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/patients")
public class PatientController {

    private final PatientService patientService;

    public PatientController(PatientService patientService){
        this.patientService = patientService;
    }
    
    @PostMapping
    public ResponseEntity<Patient> createPatient(@Valid @RequestBody CreatePatientRequest request){
        Patient patient = patientService.createPatient(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(patient);
    }

    @GetMapping
    public List<Patient> getAllPatients(){
        return patientService.getAllPatients();
    }

    @GetMapping("/{id}")
    public Patient getPatientById(@PathVariable UUID id){
        return patientService.getPatientById(id);
    }

    
    @PutMapping("/{id}")
    public Patient updatePatient(@PathVariable UUID id, @Valid @RequestBody UpdatePatientRequest request){
        return patientService.updatePatient(id, request);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePatient(@PathVariable UUID id){
        patientService.deletePatientById(id);
        return ResponseEntity.noContent().build();
    }
}
