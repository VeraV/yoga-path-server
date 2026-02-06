package com.yogapath.service;

import com.yogapath.model.Limitation;
import com.yogapath.repository.LimitationRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LimitationService {

    private final LimitationRepository limitationRepository;

    public LimitationService(LimitationRepository limitationRepository) {
        this.limitationRepository = limitationRepository;
    }

    public List<Limitation> getAllLimitations() {
        return limitationRepository.findAll();
    }
}
