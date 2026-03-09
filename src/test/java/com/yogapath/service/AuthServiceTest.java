package com.yogapath.service;

import com.yogapath.dto.LoginRequest;
import com.yogapath.dto.LoginResponse;
import com.yogapath.dto.UserRequest;
import com.yogapath.dto.UserResponse;
import com.yogapath.model.User;
import com.yogapath.repository.UserRepository;
import com.yogapath.security.TokenService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private TokenService tokenService;

    @InjectMocks
    private AuthService authService;

    private User existingUser;
    private UserRequest registerRequest;
    private LoginRequest loginRequest;

    @BeforeEach
    void setUp() {
        registerRequest = new UserRequest();
        registerRequest.setName("Vera");
        registerRequest.setEmail("vera@example.com");
        registerRequest.setPassword("secret123");

        loginRequest = new LoginRequest();
        loginRequest.setEmail("vera@example.com");
        loginRequest.setPassword("secret123");

        existingUser = new User(1L, "Vera", "vera@example.com", "hashed_secret123", true);
    }

    // --- register ---

    @Test
    void register_throwsException_whenEmailAlreadyExists() {
        when(userRepository.existsByEmail("vera@example.com")).thenReturn(true);

        assertThatThrownBy(() -> authService.register(registerRequest))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Email already exists");

        verify(userRepository, never()).save(any());
    }

    @Test
    void register_savesUserAndReturnsLoginResponse_whenEmailIsNew() {
        when(userRepository.existsByEmail("vera@example.com")).thenReturn(false);
        when(passwordEncoder.encode("secret123")).thenReturn("hashed_secret123");
        when(userRepository.save(any(User.class))).thenReturn(existingUser);
        when(tokenService.generateToken("vera@example.com", 1L)).thenReturn("jwt-token");

        LoginResponse result = authService.register(registerRequest);

        assertThat(result.getToken()).isEqualTo("jwt-token");
        assertThat(result.getUserId()).isEqualTo(1L);
        assertThat(result.getEmail()).isEqualTo("vera@example.com");
        assertThat(result.getName()).isEqualTo("Vera");
    }

    @Test
    void register_encodesPassword_beforeSaving() {
        when(userRepository.existsByEmail("vera@example.com")).thenReturn(false);
        when(passwordEncoder.encode("secret123")).thenReturn("hashed_secret123");
        when(userRepository.save(any(User.class))).thenReturn(existingUser);
        when(tokenService.generateToken(anyString(), any())).thenReturn("jwt-token");

        authService.register(registerRequest);

        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(captor.capture());
        assertThat(captor.getValue().getPasswordHash()).isEqualTo("hashed_secret123");
    }

    // --- login ---

    @Test
    void login_throwsException_whenEmailNotFound() {
        when(userRepository.findByEmail("vera@example.com")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> authService.login(loginRequest))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Invalid email or password");
    }

    @Test
    void login_throwsException_whenPasswordDoesNotMatch() {
        when(userRepository.findByEmail("vera@example.com")).thenReturn(Optional.of(existingUser));
        when(passwordEncoder.matches("secret123", "hashed_secret123")).thenReturn(false);

        assertThatThrownBy(() -> authService.login(loginRequest))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Invalid email or password");
    }

    @Test
    void login_throwsException_whenUserIsDisabled() {
        existingUser.setEnabled(false);
        when(userRepository.findByEmail("vera@example.com")).thenReturn(Optional.of(existingUser));
        when(passwordEncoder.matches("secret123", "hashed_secret123")).thenReturn(true);

        assertThatThrownBy(() -> authService.login(loginRequest))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Account is disabled");
    }

    @Test
    void login_returnsLoginResponse_whenCredentialsAreValidAndUserIsEnabled() {
        when(userRepository.findByEmail("vera@example.com")).thenReturn(Optional.of(existingUser));
        when(passwordEncoder.matches("secret123", "hashed_secret123")).thenReturn(true);
        when(tokenService.generateToken("vera@example.com", 1L)).thenReturn("jwt-token");

        LoginResponse result = authService.login(loginRequest);

        assertThat(result.getToken()).isEqualTo("jwt-token");
        assertThat(result.getUserId()).isEqualTo(1L);
        assertThat(result.getEmail()).isEqualTo("vera@example.com");
        assertThat(result.getName()).isEqualTo("Vera");
        verify(tokenService).generateToken("vera@example.com", 1L);
    }

    // --- getCurrentUser ---

    @Test
    void getCurrentUser_throwsException_whenUserIdNotFound() {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> authService.getCurrentUser(99L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("User not found");
    }

    @Test
    void getCurrentUser_returnsUserResponse_whenUserExists() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(existingUser));

        UserResponse result = authService.getCurrentUser(1L);

        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getName()).isEqualTo("Vera");
        assertThat(result.getEmail()).isEqualTo("vera@example.com");
        assertThat(result.getEnabled()).isTrue();
    }
}
