package com.aitasker.backend.dto;

import lombok.Data;
import java.util.List;

@Data
public class GeminiRequest {
    private List<Content> contents;

    @Data
    public static class Content {
        private List<Part> parts;
    }

    @Data
    public static class Part {
        private String text;
    }

    public GeminiRequest(String prompt) {
        Part part = new Part();
        part.setText(prompt);
        Content content = new Content();
        content.setParts(List.of(part));
        this.contents = List.of(content);
    }
}
