package com.oscarfndez.notifications.persistence.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "notification")
public class NotificationEntity {

    @Id
    private UUID id;

    @Column(nullable = false)
    private String type;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String message;

    @Column(nullable = false)
    private String sourceService;

    @Column(nullable = false)
    private String sourceEntityType;

    @Column(nullable = false)
    private UUID sourceEntityId;

    private String sourceEntityName;

    @Column(nullable = false)
    private Instant occurredAt;

    @Column(nullable = false)
    private Instant createdAt;

    protected NotificationEntity() {
    }

    public NotificationEntity(UUID id, String type, String title, String message, String sourceService, String sourceEntityType, UUID sourceEntityId, String sourceEntityName, Instant occurredAt, Instant createdAt) {
        this.id = id;
        this.type = type;
        this.title = title;
        this.message = message;
        this.sourceService = sourceService;
        this.sourceEntityType = sourceEntityType;
        this.sourceEntityId = sourceEntityId;
        this.sourceEntityName = sourceEntityName;
        this.occurredAt = occurredAt;
        this.createdAt = createdAt;
    }

    public UUID getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public String getTitle() {
        return title;
    }

    public String getMessage() {
        return message;
    }

    public String getSourceService() {
        return sourceService;
    }

    public String getSourceEntityType() {
        return sourceEntityType;
    }

    public UUID getSourceEntityId() {
        return sourceEntityId;
    }

    public String getSourceEntityName() {
        return sourceEntityName;
    }

    public Instant getOccurredAt() {
        return occurredAt;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }
}
