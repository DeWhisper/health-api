package health_api.controller;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import health_api.dto.CreateAppointmentRequest;
import health_api.exception.AppointmentNotFoundException;
import health_api.model.Appointment;
import health_api.model.Patient;
import health_api.service.AppointmentService;
import health_api.service.JwtService;
import tools.jackson.databind.ObjectMapper;

@WebMvcTest(AppointmentController.class)
public class AppointmentControllerTest {
    
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private JwtService jwtService;

    @MockitoBean
    private AppointmentService appointmentService;

    @Test
    void getAllAppointments_return200() throws Exception{

        when(appointmentService.getAllAppointments()).thenReturn(List.of());

        mockMvc.perform(get("/appointments")).andExpect(status().isOk());
    }

    @Test
    void getAllAppointments_returnAppointments() throws Exception{
        when(appointmentService.getAllAppointments()).thenReturn(List.of(new Appointment()));

        mockMvc.perform(get("/appointments")).andExpect(jsonPath("$[0]").exists());
    }

    @Test 
    void getAppointmentById_return200_whenAppointmentExists() throws Exception{
        Patient patient = new Patient(
            "John",
            "Smith",
            "200000000000",
            LocalDate.of(2000, 1, 1)
        );
        Appointment appointment = new Appointment(
            patient,
            LocalDate.of(2000, 1, 1),
            "Sick"
        );

        UUID id = UUID.randomUUID();

        when(appointmentService.getAppointmentById(id)).thenReturn(appointment);
        mockMvc.perform(get("/appointments/" + id)).andExpect(status().isOk());
    }

    @Test
    void getAppointmentById_return404_whenAppointmentDoesNotExist() throws Exception{
        UUID id = UUID.randomUUID();
        when(appointmentService.getAppointmentById(id))
            .thenThrow(new AppointmentNotFoundException("Appointment not found with id: " + id));
        mockMvc.perform(get("/appointments/" + id)).andExpect(status().isNotFound());
    }

    @Test
    void createdAppointment_return201_whenRequestIsValid() throws Exception{
        UUID id = UUID.randomUUID();
        Patient patient = new Patient(
            "John",
            "Smith",
            "200000000000",
            LocalDate.of(2000, 1, 1)
        );
        Appointment appointment = new Appointment(
            patient,
            LocalDate.of(2000, 1, 1),
            "Sick"
        );
        CreateAppointmentRequest request = new CreateAppointmentRequest(
            id,
            LocalDate.of(2000, 1, 1),
            "Sick"
        );
        when(appointmentService.createAppointment(request)).thenReturn(appointment);

        mockMvc.perform(post("/appointments")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isCreated());
        verify(appointmentService).createAppointment(request);
    }

    @Test
    void createAppointment_return400_whenRequestIsInvalid() throws Exception{
        UUID id = UUID.randomUUID();
        // Missing reason
        CreateAppointmentRequest request = new CreateAppointmentRequest(
            id,
            LocalDate.of(2000, 1, 1),
            ""
        );
        mockMvc.perform(post("/appointments")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isBadRequest());
    }

    @Test
    void deleteAppointment_return204_whenAppointmentExists() throws Exception{
        UUID id = UUID.randomUUID();
        mockMvc.perform(delete("/appointments/" + id)).andExpect(status().isNoContent());
    }

    @Test
    void deleteAppointment_return404_whenAppointmentDoesNotExist() throws Exception{
        UUID id = UUID.randomUUID();

        doThrow(new AppointmentNotFoundException("Appointment not found with id: " + id))
            .when(appointmentService)
            .deleteAppointmentById(id);
        
        mockMvc.perform(delete("/appointments/" + id))
            .andExpect(status().isNotFound());
    }
}
