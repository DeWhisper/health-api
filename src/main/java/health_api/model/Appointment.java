package health_api.model;

import java.time.LocalDate;
import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity 
public class Appointment {
    
    @Id 
    @GeneratedValue 
    private UUID id; 

    private LocalDate date;

    private String reason;

    @ManyToOne 
    private Patient patient;

    public Appointment(Patient patient, LocalDate date, String reason){
        this.patient = patient;
        this.date = date;
        this.reason = reason;
    }

    public Appointment(){}

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }
}
