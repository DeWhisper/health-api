package health_api.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import health_api.dto.CreateAppointmentRequest;
import health_api.dto.UpdateAppointmentRequest;
import health_api.exception.AppointmentNotFoundException;
import health_api.exception.PatientNotFoundException;
import health_api.model.Appointment;
import health_api.model.Patient;
import health_api.repository.AppointmentRepository;
import health_api.repository.PatientRepository;

@Service 
public class AppointmentService {

    private AppointmentRepository repo;

    private PatientRepository patientRepo;

    public AppointmentService(AppointmentRepository repo, PatientRepository patientRepo){
        this.repo = repo;
        this.patientRepo = patientRepo;
    }

    public Appointment createAppointment(CreateAppointmentRequest request){
        Patient patient = patientRepo.findById(request.patientId())
            .orElseThrow(() -> new PatientNotFoundException("Patient not found with id: " + request.patientId()));

        Appointment appointment = new Appointment(patient, request.date(), request.reason());
        return repo.save(appointment);
    }

    public List<Appointment> getAllAppointments(){
        return repo.findAll();
    }

    public Appointment getAppointmentById(UUID id){
        return repo.findById(id)
            .orElseThrow(() -> new AppointmentNotFoundException("Appointment not found with id: " + id));
    }

    public Appointment updateAppointment(UUID id, UpdateAppointmentRequest request){
        Appointment appointment = repo.findById(id)
            .orElseThrow(() -> new AppointmentNotFoundException("Appointment not found with id: " + id));

        appointment.setDate(request.date());
        appointment.setReason(request.reason());

        return repo.save(appointment);
    }

    public void deleteAppointmentById(UUID id){
        Appointment appointment = repo.findById(id)
            .orElseThrow(() -> new AppointmentNotFoundException("Appointment not found with id: " + id));
        repo.delete(appointment);
    }

}
