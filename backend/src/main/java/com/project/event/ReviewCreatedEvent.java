package com.project.event;
import java.util.UUID;
public record ReviewCreatedEvent(UUID reviewId, UUID clientAccountId, UUID expertAccountId, Integer score) {}
