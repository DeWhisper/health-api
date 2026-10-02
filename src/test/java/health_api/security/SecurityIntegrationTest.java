package health_api.security;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

import health_api.model.User;
import health_api.service.JwtService;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest 
@AutoConfigureMockMvc 
public class SecurityIntegrationTest {
    @Autowired 
    private MockMvc mockMvc;

    @Autowired 
    private JwtService jwtService;

    @Test
    void userCannotDeletePatient() throws Exception{
        User user = new User("testuser", "password", "USER");
        String token = jwtService.generateToken(user);

        mockMvc.perform(delete("/patients/00000000-0000-0000-0000-000000000001")
            .header("Authorization", "Bearer " + token))
            .andExpect(status().isForbidden());
    }

    @Test
    void userCannotDeleteAppointment() throws Exception{
        User user = new User("testuser", "password", "USER");
        String token = jwtService.generateToken(user);

        mockMvc.perform(delete("/appointments/00000000-0000-0000-0000-000000000001")
            .header("Authorization", "Bearer " + token))
            .andExpect(status().isForbidden());
    }

    @Test
    void adminCanDeletePatient() throws Exception{
        User admin = new User("testuser", "password", "ADMIN");
        String token = jwtService.generateToken(admin);

        mockMvc.perform(delete("/patients/00000000-0000-0000-0000-000000000001")
            .header("Authorization", "Bearer " + token))
            .andExpect(status().isNotFound());
    }

    @Test
    void adminCanDeleteAppointment() throws Exception{
        User admin = new User("testuser", "password", "ADMIN");
        String token = jwtService.generateToken(admin);

        mockMvc.perform(delete("/appointments/00000000-0000-0000-0000-000000000001")
            .header("Authorization", "Bearer " + token))
            .andExpect(status().isNotFound());
    }
}
