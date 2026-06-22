package com.project.event;

public record ReviewCreatedEvent(Long reviewId, Long clientId, Long expertId, Integer score) {
}
