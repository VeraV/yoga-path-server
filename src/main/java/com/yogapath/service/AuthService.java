package com.yogapath.service;

import com.yogapath.dto.LoginRequest;
import com.yogapath.dto.LoginResponse;
import com.yogapath.dto.UserRequest;
import com.yogapath.dto.UserResponse;
import com.yogapath.model.User;
import com.yogapath.repository.UserRepository;
import com.yogapath.security.TokenService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;

    public AuthService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       TokenService tokenService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenService = tokenService;
    }

    public LoginResponse register(UserRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already exists: " + request.getEmail());
        }

        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        user.setEnabled(true);

        User saved = userRepository.save(user);

        // Generate token for auto-login after registration
        String token = tokenService.generateToken(saved.getEmail(), saved.getId());

        return new LoginResponse(token, saved.getId(), saved.getEmail(), saved.getName());
    }

    public LoginResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Invalid email or password"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
            throw new RuntimeException("Invalid email or password");
        }

        if (!user.getEnabled()) {
            throw new RuntimeException("Account is disabled");
        }

        String token = tokenService.generateToken(user.getEmail(), user.getId());

        return new LoginResponse(token, user.getId(), user.getEmail(), user.getName());
    }

    public UserResponse getCurrentUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return new UserResponse(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getEnabled(),
                user.getCreatedAt()
        );
    }
}
