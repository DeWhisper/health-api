package health_api.controller;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import health_api.dto.CreatePatientRequest;
import health_api.exception.PatientNotFoundException;
import health_api.model.Patient;
import health_api.service.JwtService;
import health_api.service.PatientService;
import tools.jackson.databind.ObjectMapper;

@WebMvcTest(PatientController.class)
public class PatientControllerTest {
    
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private JwtService jwtService;

    @MockitoBean
    private PatientService patientService;

    @Test
    void getAllPatients_return200() throws Exception{

        when(patientService.getAllPatients()).thenReturn(List.of());

        mockMvc.perform(get("/patients")).andExpect(status().isOk());
    }

    @Test 
    void getAllPatients_returnPatients() throws Exception{
        when(patientService.getAllPatients()).thenReturn(List.of(new Patient()));

        mockMvc.perform(get("/patients")).andExpect(jsonPath("$[0]").exists());
    }

    @Test 
    void getPatientById_return200_whenPatientExist() throws Exception{
        Patient patient = new Patient(
            "John",
            "Smith",
            "200000000000",
            LocalDate.of(2000, 1, 1)
        );

        UUID id = UUID.randomUUID();

        when(patientService.getPatientById(id)).thenReturn(patient);

        mockMvc.perform(get("/patients/" + id)).andExpect(status().isOk());
        //TODO: add verification of right patient: jsonPath("$.firstName").value(""))
    }

    @Test 
    void getPatientById_return404_whenPatientDoesNotExist() throws Exception{
        UUID id = UUID.randomUUID();

        when(patientService.getPatientById(id))
            .thenThrow(new PatientNotFoundException("Patient not foud with id: " + id));
        
        mockMvc.perform(get("/patients/" + id)).andExpect(status().isNotFound());
    }

    @Test 
    void createPatient_return201_whenRequestIsValid() throws Exception{
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

        when(patientService.createPatient(request)).thenReturn(patient);

        mockMvc.perform(post("/patients")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isCreated());
    }

    @Test 
    void createPatient_return400_whenRequestIsInvalid() throws Exception{
        CreatePatientRequest request = new CreatePatientRequest(
            "",
            "Smith",
            "200000000000",
            LocalDate.of(2000, 1, 1)
        );

        mockMvc.perform(post("/patients")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isBadRequest());
    }

    @Test 
    void deletePatient_returns204_whenPatientExists() throws Exception{
        UUID id = UUID.randomUUID();
        mockMvc.perform(delete("/patients/" + id)).andExpect(status().isNoContent());
    }

    @Test 
    void deletePatient_returns404_whenPatientDoesNotExist() throws Exception{
        UUID id = UUID.randomUUID();

        doThrow(new PatientNotFoundException("Patient not found with id: " + id))
            .when(patientService)
            .deletePatientById(id);

        mockMvc.perform(delete("/patients/" + id))
            .andExpect(status().isNotFound());
    }

}
