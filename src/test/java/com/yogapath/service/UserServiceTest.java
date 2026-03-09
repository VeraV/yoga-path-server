package com.yogapath.service;

import com.yogapath.dto.UserRequest;
import com.yogapath.dto.UserResponse;
import com.yogapath.model.User;
import com.yogapath.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private User savedUser;
    private UserRequest request;

    @BeforeEach
    void setUp() {
        request = new UserRequest();
        request.setName("Vera");
        request.setEmail("vera@example.com");
        request.setPassword("secret123");

        savedUser = new User(1L, "Vera", "vera@example.com", "secret123", true);
        savedUser.setCreatedAt(LocalDateTime.of(2026, 1, 1, 10, 0));
    }

    // --- createUser ---

    @Test
    void createUser_returnsUserResponse_whenEmailIsNew() {
        when(userRepository.existsByEmail("vera@example.com")).thenReturn(false);
        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        UserResponse result = userService.createUser(request);

        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getName()).isEqualTo("Vera");
        assertThat(result.getEmail()).isEqualTo("vera@example.com");
        assertThat(result.getEnabled()).isTrue();
    }

    @Test
    void createUser_throwsException_whenEmailAlreadyExists() {
        when(userRepository.existsByEmail("vera@example.com")).thenReturn(true);

        assertThatThrownBy(() -> userService.createUser(request))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Email already exists");

        verify(userRepository, never()).save(any());
    }

    @Test
    void createUser_savesUserWithCorrectFields() {
        when(userRepository.existsByEmail("vera@example.com")).thenReturn(false);
        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        userService.createUser(request);

        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(captor.capture());

        User captured = captor.getValue();
        assertThat(captured.getName()).isEqualTo("Vera");
        assertThat(captured.getEmail()).isEqualTo("vera@example.com");
        assertThat(captured.getEnabled()).isTrue();
    }

    // --- getUserById ---

    @Test
    void getUserById_returnsUserResponse_whenUserExists() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(savedUser));

        UserResponse result = userService.getUserById(1L);

        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getName()).isEqualTo("Vera");
        assertThat(result.getEmail()).isEqualTo("vera@example.com");
    }

    @Test
    void getUserById_throwsException_whenUserNotFound() {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.getUserById(99L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("User not found with id: 99");
    }

    @Test
    void getUserById_mapsCreatedAtCorrectly() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(savedUser));

        UserResponse result = userService.getUserById(1L);

        assertThat(result.getCreatedAt()).isEqualTo(LocalDateTime.of(2026, 1, 1, 10, 0));
    }
}
