package pl.krutkowski.auth.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.krutkowski.auth.dto.AuthRequest;
import pl.krutkowski.auth.dto.AuthResponse;
import pl.krutkowski.auth.dto.RegisterRequest;
import pl.krutkowski.auth.model.Role;
import pl.krutkowski.auth.model.User;
import pl.krutkowski.auth.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthResponse register(RegisterRequest request) {
        if(userRepository.existsByUsername(request.username())) {
            throw new RuntimeException("Username already taken");
        }

        User user = new User();
        user.setUsername(request.username());
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setRole(Role.USER);
        userRepository.save(user);

        String token = jwtService.generateToken(user);

        return new AuthResponse(token);
    }

    public AuthResponse authenticate(AuthRequest request) {
        // Autoryzacja przez AuthenticationManager (sprawdza hasło i username)
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.username(), request.password())
        );

        User user = userRepository.findByUsername(request.username())
                .orElseThrow(() -> new RuntimeException("User not found"));

        String token = jwtService.generateToken(user);

        return new AuthResponse(token);
    }
}

