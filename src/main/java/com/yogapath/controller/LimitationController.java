package com.yogapath.controller;

import com.yogapath.model.Limitation;
import com.yogapath.service.LimitationService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/limitations")
public class LimitationController {

    private final LimitationService limitationService;

    public LimitationController(LimitationService limitationService) {
        this.limitationService = limitationService;
    }

    @GetMapping
    public List<Limitation> getAllLimitations() {
        return limitationService.getAllLimitations();
    }
}
