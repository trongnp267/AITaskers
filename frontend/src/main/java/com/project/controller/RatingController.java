package com.project.controller;

import com.project.dto.RatingDTO;
import com.project.model.Rating;
import com.project.service.IRatingService;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RatingController {
    private final IRatingService ratingService;

    public RatingController(IRatingService ratingService) {
        this.ratingService = ratingService;
    }

    @PostMapping("/ratings")
    @ResponseStatus(HttpStatus.CREATED)
    public Map<String, Object> createRating(@RequestBody RatingDTO ratingDTO) {
        Rating rating = ratingService.createRating(ratingDTO);
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("success", true);
        response.put("message", "Danh gia da duoc ghi nhan.");
        response.put("data", rating.toMap());
        return response;
    }

    @GetMapping("/ratings")
    public Map<String, Object> listRatings(@RequestParam(required = false) String paymentId) {
        List<Rating> ratings = ratingService.findRatings(paymentId);
        List<Map<String, Object>> data = new ArrayList<>();

        for (Rating rating : ratings) {
            data.add(rating.toMap());
        }

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("success", true);
        response.put("total", data.size());
        response.put("data", data);
        return response;
    }
}
