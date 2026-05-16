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
    private String titleKey;

    @Column(nullable = false)
    private String messageKey;

    @Column(nullable = false)
    private String paramsJson;

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

    public NotificationEntity(UUID id, String type, String titleKey, String messageKey, String paramsJson, String sourceService, String sourceEntityType, UUID sourceEntityId, String sourceEntityName, Instant occurredAt, Instant createdAt) {
        this.id = id;
        this.type = type;
        this.titleKey = titleKey;
        this.messageKey = messageKey;
        this.paramsJson = paramsJson;
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

    public String getTitleKey() {
        return titleKey;
    }

    public String getMessageKey() {
        return messageKey;
    }

    public String getParamsJson() {
        return paramsJson;
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
