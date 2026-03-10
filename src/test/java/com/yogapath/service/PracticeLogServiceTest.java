package com.yogapath.service;

import com.yogapath.dto.PracticeLogRequest;
import com.yogapath.dto.PracticeLogResponse;
import com.yogapath.model.PracticeLog;
import com.yogapath.model.User;
import com.yogapath.repository.PracticeLogRepository;
import com.yogapath.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PracticeLogServiceTest {

    @Mock
    private PracticeLogRepository practiceLogRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private PracticeLogService practiceLogService;

    private User user;
    private PracticeLog log1;
    private PracticeLog log2;
    private PracticeLogRequest request;

    @BeforeEach
    void setUp() {
        user = new User(1L, "Vera", "vera@example.com", "hashed", true);

        log1 = new PracticeLog();
        log1.setId(1L);
        log1.setUser(user);
        log1.setPracticeDate(LocalDate.of(2026, 3, 10));
        log1.setMinutesPracticed(60);
        log1.setNotes("Morning practice");
        log1.setCreatedAt(LocalDateTime.of(2026, 3, 10, 8, 0));

        log2 = new PracticeLog();
        log2.setId(2L);
        log2.setUser(user);
        log2.setPracticeDate(LocalDate.of(2026, 3, 9));
        log2.setMinutesPracticed(30);
        log2.setNotes("Evening practice");
        log2.setCreatedAt(LocalDateTime.of(2026, 3, 9, 19, 0));

        request = new PracticeLogRequest();
        request.setUserId(1L);
        request.setPracticeDate(LocalDate.of(2026, 3, 10));
        request.setMinutesPracticed(60);
        request.setNotes("Morning practice");
    }

    // --- createPracticeLog ---

    @Test
    void createPracticeLog_throwsException_whenUserNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> practiceLogService.createPracticeLog(request))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("User not found with id: 1");

        verify(practiceLogRepository, never()).save(any());
    }

    @Test
    void createPracticeLog_savesLogAndReturnsResponse_whenUserExists() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(practiceLogRepository.save(any(PracticeLog.class))).thenReturn(log1);

        PracticeLogResponse result = practiceLogService.createPracticeLog(request);

        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getUserId()).isEqualTo(1L);
        assertThat(result.getPracticeDate()).isEqualTo(LocalDate.of(2026, 3, 10));
        assertThat(result.getMinutesPracticed()).isEqualTo(60);
        assertThat(result.getNotes()).isEqualTo("Morning practice");
    }

    @Test
    void createPracticeLog_savesLogWithCorrectFields() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(practiceLogRepository.save(any(PracticeLog.class))).thenReturn(log1);

        practiceLogService.createPracticeLog(request);

        ArgumentCaptor<PracticeLog> captor = ArgumentCaptor.forClass(PracticeLog.class);
        verify(practiceLogRepository).save(captor.capture());

        PracticeLog captured = captor.getValue();
        assertThat(captured.getUser()).isEqualTo(user);
        assertThat(captured.getPracticeDate()).isEqualTo(LocalDate.of(2026, 3, 10));
        assertThat(captured.getMinutesPracticed()).isEqualTo(60);
    }

    // --- getPracticeLogById ---

    @Test
    void getPracticeLogById_returnsResponse_whenLogExists() {
        when(practiceLogRepository.findById(1L)).thenReturn(Optional.of(log1));

        PracticeLogResponse result = practiceLogService.getPracticeLogById(1L);

        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getMinutesPracticed()).isEqualTo(60);
    }

    @Test
    void getPracticeLogById_throwsException_whenLogNotFound() {
        when(practiceLogRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> practiceLogService.getPracticeLogById(99L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Practice log not found with id: 99");
    }

    // --- getPracticeLogsByUserId ---

    @Test
    void getPracticeLogsByUserId_returnsListOfResponses() {
        when(practiceLogRepository.findByUserIdOrderByPracticeDateDesc(1L))
                .thenReturn(List.of(log1, log2));

        List<PracticeLogResponse> result = practiceLogService.getPracticeLogsByUserId(1L);

        assertThat(result).hasSize(2);
        assertThat(result.get(0).getId()).isEqualTo(1L);
        assertThat(result.get(1).getId()).isEqualTo(2L);
    }

    @Test
    void getPracticeLogsByUserId_returnsEmptyList_whenNoLogsExist() {
        when(practiceLogRepository.findByUserIdOrderByPracticeDateDesc(1L))
                .thenReturn(List.of());

        List<PracticeLogResponse> result = practiceLogService.getPracticeLogsByUserId(1L);

        assertThat(result).isEmpty();
    }

    // --- getPracticeLogsByDateRange ---

    @Test
    void getPracticeLogsByDateRange_returnsLogsWithinRange() {
        LocalDate start = LocalDate.of(2026, 3, 1);
        LocalDate end = LocalDate.of(2026, 3, 10);

        when(practiceLogRepository.findByUserIdAndPracticeDateBetween(1L, start, end))
                .thenReturn(List.of(log1, log2));

        List<PracticeLogResponse> result = practiceLogService.getPracticeLogsByDateRange(1L, start, end);

        assertThat(result).hasSize(2);
    }

    @Test
    void getPracticeLogsByDateRange_returnsEmptyList_whenNoLogsInRange() {
        LocalDate start = LocalDate.of(2026, 1, 1);
        LocalDate end = LocalDate.of(2026, 1, 31);

        when(practiceLogRepository.findByUserIdAndPracticeDateBetween(1L, start, end))
                .thenReturn(List.of());

        List<PracticeLogResponse> result = practiceLogService.getPracticeLogsByDateRange(1L, start, end);

        assertThat(result).isEmpty();
    }

    // --- updatePracticeLog ---

    @Test
    void updatePracticeLog_throwsException_whenLogNotFound() {
        when(practiceLogRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> practiceLogService.updatePracticeLog(99L, request))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Practice log not found with id: 99");

        verify(practiceLogRepository, never()).save(any());
    }

    @Test
    void updatePracticeLog_updatesFieldsAndReturnsResponse() {
        PracticeLogRequest updateRequest = new PracticeLogRequest();
        updateRequest.setUserId(1L);
        updateRequest.setPracticeDate(LocalDate.of(2026, 3, 11));
        updateRequest.setMinutesPracticed(90);
        updateRequest.setNotes("Updated notes");

        PracticeLog updatedLog = new PracticeLog();
        updatedLog.setId(1L);
        updatedLog.setUser(user);
        updatedLog.setPracticeDate(LocalDate.of(2026, 3, 11));
        updatedLog.setMinutesPracticed(90);
        updatedLog.setNotes("Updated notes");

        when(practiceLogRepository.findById(1L)).thenReturn(Optional.of(log1));
        when(practiceLogRepository.save(any(PracticeLog.class))).thenReturn(updatedLog);

        PracticeLogResponse result = practiceLogService.updatePracticeLog(1L, updateRequest);

        assertThat(result.getMinutesPracticed()).isEqualTo(90);
        assertThat(result.getNotes()).isEqualTo("Updated notes");
        assertThat(result.getPracticeDate()).isEqualTo(LocalDate.of(2026, 3, 11));
    }

    // --- deletePracticeLog ---

    @Test
    void deletePracticeLog_throwsException_whenLogNotFound() {
        when(practiceLogRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> practiceLogService.deletePracticeLog(99L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Practice log not found with id: 99");

        verify(practiceLogRepository, never()).delete(any());
    }

    @Test
    void deletePracticeLog_deletesLog_whenLogExists() {
        when(practiceLogRepository.findById(1L)).thenReturn(Optional.of(log1));

        practiceLogService.deletePracticeLog(1L);

        verify(practiceLogRepository).delete(log1);
    }
}
