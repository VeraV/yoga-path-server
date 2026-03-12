package com.yogapath.controller;

import com.yogapath.model.YogaStyle;
import com.yogapath.security.JwtAuthenticationFilter;
import com.yogapath.service.YogaStyleService;
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

@WebMvcTest(YogaStyleController.class)
@Import(SecurityConfig.class)
class YogaStyleControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private YogaStyleService yogaStyleService;

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
    void getAllYogaStyles_returnsOkWithListOfStyles() throws Exception {
        YogaStyle style1 = new YogaStyle(1L, "Ashtanga", "Dynamic style of yoga", "Physically demanding");
        YogaStyle style2 = new YogaStyle(2L, "Sivananda", "Static + dynamic style", "The best yoga style");

        when(yogaStyleService.getAllYogaStyles()).thenReturn(List.of(style1, style2));

        mockMvc.perform(get("/api/yoga-styles"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Ashtanga"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].name").value("Sivananda"));
    }

    @Test
    void getAllYogaStyles_returnsOkWithEmptyList_whenNoStylesExist() throws Exception {
        when(yogaStyleService.getAllYogaStyles()).thenReturn(List.of());

        mockMvc.perform(get("/api/yoga-styles"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    void getAllYogaStyles_returnsJsonContentType() throws Exception {
        when(yogaStyleService.getAllYogaStyles()).thenReturn(List.of());

        mockMvc.perform(get("/api/yoga-styles"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"));
    }
}
