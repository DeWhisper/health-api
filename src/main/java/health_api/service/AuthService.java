package health_api.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import health_api.dto.LoginRequest;
import health_api.dto.LoginResponse;
import health_api.dto.RegisterRequest;
import health_api.model.User;
import health_api.repository.UserRepository;

@Service 
public class AuthService {
    
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService){
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public User register(RegisterRequest request){

        if(userRepository.findByUsername(request.username()).isPresent()){
            throw new RuntimeException("Username already exists");
        }

        String hashedPassword = passwordEncoder.encode(request.password());

        User user = new User(request.username(), hashedPassword, "USER");

        return userRepository.save(user);
    }

    public LoginResponse login(LoginRequest request){
        User user = userRepository.findByUsername(request.username())
            .orElseThrow(() -> new RuntimeException("Invalid username or password"));
        
        if(!passwordEncoder.matches(request.password(), user.getPassword())){
            throw new RuntimeException("Invalid username or password");
        }

        String token = jwtService.generateToken(user);

        return new LoginResponse(token);
    }
}
