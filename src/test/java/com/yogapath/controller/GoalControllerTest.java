package com.yogapath.controller;

import com.yogapath.model.Goal;
import com.yogapath.security.JwtAuthenticationFilter;
import com.yogapath.service.GoalService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import com.yogapath.config.SecurityConfig;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(GoalController.class)
@Import(SecurityConfig.class)
class GoalControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GoalService goalService;

    @MockBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @BeforeEach
    void setUp() throws Exception {
        // Make the mock filter pass the request through to the controller
        doAnswer(invocation -> {
            FilterChain chain = invocation.getArgument(2);
            chain.doFilter(invocation.getArgument(0), invocation.getArgument(1));
            return null;
        }).when(jwtAuthenticationFilter).doFilter(
                any(ServletRequest.class),
                any(ServletResponse.class),
                any(FilterChain.class)
        );
    }

    @Test
    void getAllGoals_returnsOkWithListOfGoals() throws Exception {
        Goal goal1 = new Goal(1L, "Flexibility", "Improve flexibility", null);
        Goal goal2 = new Goal(2L, "Strength", "Build core strength", "Focus on plank poses");

        when(goalService.getAllGoals()).thenReturn(List.of(goal1, goal2));

        mockMvc.perform(get("/api/goals"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Flexibility"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].name").value("Strength"));
    }

    @Test
    void getAllGoals_returnsOkWithEmptyList_whenNoGoalsExist() throws Exception {
        when(goalService.getAllGoals()).thenReturn(List.of());

        mockMvc.perform(get("/api/goals"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    void getAllGoals_returnsJsonContentType() throws Exception {
        when(goalService.getAllGoals()).thenReturn(List.of());

        mockMvc.perform(get("/api/goals"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"));
    }
}
