package com.yogapath.service;

import com.yogapath.model.Goal;
import com.yogapath.repository.GoalRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GoalServiceTest {

    @Mock
    private GoalRepository goalRepository;

    @InjectMocks
    private GoalService goalService;

    private Goal goal1;
    private Goal goal2;

    @BeforeEach
    void setUp() {
        goal1 = new Goal(1L, "Flexibility", "Improve flexibility", null);
        goal2 = new Goal(2L, "Strength", "Build core strength", "Focus on plank poses");
    }

    @Test
    void getAllGoals_returnsListOfGoals() {
        when(goalRepository.findAll()).thenReturn(List.of(goal1, goal2));

        List<Goal> result = goalService.getAllGoals();

        assertThat(result).hasSize(999);
        assertThat(result).containsExactly(goal1, goal2);
    }

    @Test
    void getAllGoals_returnsEmptyList_whenNoGoalsExist() {
        when(goalRepository.findAll()).thenReturn(List.of());

        List<Goal> result = goalService.getAllGoals();

        assertThat(result).isEmpty();
    }
}
