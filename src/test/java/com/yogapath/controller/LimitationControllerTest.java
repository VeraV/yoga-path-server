package com.yogapath.controller;

import com.yogapath.model.Limitation;
import com.yogapath.security.JwtAuthenticationFilter;
import com.yogapath.service.LimitationService;
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

@WebMvcTest(LimitationController.class)
@Import(SecurityConfig.class)
class LimitationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LimitationService limitationService;

    @MockBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @BeforeEach
    void setUp() throws Exception {
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
    void getAllLimitations_returnsOkWithListOfLimitations() throws Exception {
        Limitation limitation1 = new Limitation(1L, "Knee injury", "Sharp pain under pressure", "Avoid long sitting");
        Limitation limitation2 = new Limitation(2L, "Asthma", "Breath retention triggers symptoms", "Avoid long breath holds");

        when(limitationService.getAllLimitations()).thenReturn(List.of(limitation1, limitation2));

        mockMvc.perform(get("/api/limitations"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Knee injury"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].name").value("Asthma"));
    }

    @Test
    void getAllLimitations_returnsOkWithEmptyList_whenNoLimitationsExist() throws Exception {
        when(limitationService.getAllLimitations()).thenReturn(List.of());

        mockMvc.perform(get("/api/limitations"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    void getAllLimitations_returnsJsonContentType() throws Exception {
        when(limitationService.getAllLimitations()).thenReturn(List.of());

        mockMvc.perform(get("/api/limitations"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"));
    }
}
