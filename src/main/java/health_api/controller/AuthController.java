package health_api.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import health_api.dto.LoginRequest;
import health_api.dto.RegisterRequest;
import health_api.dto.UserResponse;
import health_api.model.User;
import health_api.service.AuthService;
import jakarta.validation.Valid;

@RestController 
@RequestMapping("/auth")
public class AuthController{

    public final AuthService authService;

    public AuthController(AuthService authService){
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(@Valid @RequestBody RegisterRequest request){
        User user = authService.register(request);

        UserResponse response = new UserResponse(
          user.getId(),
          user.getUsername()  
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request){
        return ResponseEntity.ok(authService.login(request));
    } 
}