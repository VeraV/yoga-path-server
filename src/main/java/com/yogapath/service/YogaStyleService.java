package com.yogapath.service;

import com.yogapath.model.YogaStyle;
import com.yogapath.repository.YogaStyleRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class YogaStyleService {

    private final YogaStyleRepository yogaStyleRepository;

    public YogaStyleService(YogaStyleRepository yogaStyleRepository) {
        this.yogaStyleRepository = yogaStyleRepository;
    }

    public List<YogaStyle> getAllYogaStyles() {
        return yogaStyleRepository.findAll();
    }
}
