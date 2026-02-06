package com.yogapath.controller;

import com.yogapath.model.YogaStyle;
import com.yogapath.service.YogaStyleService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/yoga-styles")
public class YogaStyleController {

    private final YogaStyleService yogaStyleService;

    public YogaStyleController(YogaStyleService yogaStyleService) {
        this.yogaStyleService = yogaStyleService;
    }

    @GetMapping
    public List<YogaStyle> getAllYogaStyles() {
        return yogaStyleService.getAllYogaStyles();
    }
}
