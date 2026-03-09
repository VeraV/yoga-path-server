package com.yogapath.service;

import com.yogapath.model.Limitation;
import com.yogapath.repository.LimitationRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class LimitationServiceTest {

    @Mock
    private LimitationRepository limitationRepository;

    @InjectMocks
    private LimitationService limitationService;

    private Limitation limitation1;
    private Limitation limitation2;

    @BeforeEach
    void setUp() {
        limitation1 = new Limitation(1L, "knee injury", "sharp pain when under the preasure",
                "avoid long sitting, use props");
        limitation2 = new Limitation(2L, "asthma", "Intense breath retention can trigger symptoms",
                "avoid long breath holds");
    }

    @Test
    void getAllLimitations_returnsListOfLimitations() {
        when(limitationRepository.findAllByOrderByNameAsc()).thenReturn(List.of(limitation2, limitation1));

        List<Limitation> result = limitationService.getAllLimitations();

        assertThat(result).hasSize(2);
        assertThat(result).containsExactly(limitation2, limitation1);
    }

    @Test
    void getAllLimitations_returnsEmptyList_whenNoLimitationsExist() {
        when(limitationRepository.findAllByOrderByNameAsc()).thenReturn(List.of());

        List<Limitation> result = limitationService.getAllLimitations();

        assertThat(result).isEmpty();
    }

}