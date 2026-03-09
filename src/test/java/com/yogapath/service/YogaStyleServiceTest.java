package com.yogapath.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.yogapath.model.YogaStyle;
import com.yogapath.repository.YogaStyleRepository;

@ExtendWith(MockitoExtension.class)
class YogaStyleServiceTest {

    @Mock
    private YogaStyleRepository yogaStyleRepository;

    @InjectMocks
    private YogaStyleService yogaStyleService;

    private YogaStyle yogaStyle1;
    private YogaStyle yogaStyle2;

    @BeforeEach
    void setUp() {
        yogaStyle1 = new YogaStyle(1L, "Ashtanga", "Dynamic style of yoga", "Physically demanding");
        yogaStyle2 = new YogaStyle(2L, "Sivananda", "Static + dynamic style", "The best yoga style");
    }

    @Test
    void getAllYogaStyles_returnsListOfStyles() {
        when(yogaStyleRepository.findAll()).thenReturn(List.of(yogaStyle1, yogaStyle2));

        List<YogaStyle> result = yogaStyleService.getAllYogaStyles();

        assertThat(result).hasSize(2);
        assertThat(result).containsExactly(yogaStyle1, yogaStyle2);
    }

    @Test
    void getAllYogaStyles_returnsEmptyList_whenNoStylesExist() {
        when(yogaStyleRepository.findAll()).thenReturn(List.of());

        List<YogaStyle> result = yogaStyleService.getAllYogaStyles();

        assertThat(result).isEmpty();
    }

}
