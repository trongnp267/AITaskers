package com.example.demo.dto;
import lombok.Data;

@Data
public class ExpertResult {
    private Long id;
    private int matchScore;
    private String reasoning;
}