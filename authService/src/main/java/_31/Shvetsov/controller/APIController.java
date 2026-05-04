package _31.Shvetsov.controller;

import _31.Shvetsov.document.UserDocument;
import _31.Shvetsov.repository.UserRepository;
import _31.Shvetsov.schema.response.VerifiedJwtResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Comparator;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class APIController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @GetMapping("/clerk_jwt")
    public @ResponseBody VerifiedJwtResponse clerk_jwt(@AuthenticationPrincipal String userId) {
        return new VerifiedJwtResponse(userId);
    }

    @GetMapping("/gated_data")
    public @ResponseBody Map<String, String> get_gated_data() {
        return Map.of("foo", "bar");
    }

    @PostMapping("/auth/register")
    public ResponseEntity<?> register(@RequestBody AuthRequest request) {
        if (request.email() == null || request.email().isBlank() || request.password() == null || request.password().isBlank()) {
            return ResponseEntity.badRequest().body(Map.of("error", "email and password are required"));
        }
        if (userRepository.findByEmail(request.email()).isPresent()) {
            return ResponseEntity.status(409).body(Map.of("error", "email already exists"));
        }

        UserDocument user = new UserDocument();
        user.setUser_id(nextUserId());
        user.setEmail(request.email());
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setRole(request.role() == null || request.role().isBlank() ? "USER" : request.role());
        userRepository.save(user);

        return ResponseEntity.ok(new AuthResponse(user.getUser_id(), user.getEmail(), user.getRole()));
    }

    @PostMapping("/auth/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest request) {
        return userRepository.findByEmail(request.email())
                .filter(user -> passwordEncoder.matches(request.password(), user.getPassword()))
                .<ResponseEntity<?>>map(user -> ResponseEntity.ok(new AuthResponse(user.getUser_id(), user.getEmail(), user.getRole())))
                .orElseGet(() -> ResponseEntity.status(401).body(Map.of("error", "invalid email or password")));
    }

    @GetMapping("/auth/users")
    public ResponseEntity<?> users() {
        return ResponseEntity.ok(userRepository.findAll().stream()
                .map(user -> new AuthResponse(user.getUser_id(), user.getEmail(), user.getRole()))
                .toList());
    }

    @GetMapping("/auth/users/{userId}")
    public ResponseEntity<?> user(@PathVariable Integer userId) {
        return userRepository.findById(userId)
                .<ResponseEntity<?>>map(user -> ResponseEntity.ok(new AuthResponse(user.getUser_id(), user.getEmail(), user.getRole())))
                .orElseGet(() -> ResponseEntity.status(404).body(Map.of("error", "user not found")));
    }

    private Integer nextUserId() {
        return userRepository.findAll().stream()
                .map(UserDocument::getUser_id)
                .max(Comparator.naturalOrder())
                .orElse(0) + 1;
    }

    public record AuthRequest(String email, String password, String role) {
    }

    public record AuthResponse(Integer user_id, String email, String role) {
    }

}
