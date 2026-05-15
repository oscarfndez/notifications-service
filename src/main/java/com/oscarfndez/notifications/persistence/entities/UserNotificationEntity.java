package com.oscarfndez.notifications.persistence.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "user_notification")
public class UserNotificationEntity {

    @Id
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "notification_id", nullable = false)
    private NotificationEntity notification;

    @Column(nullable = false)
    private UUID userId;

    @Column(name = "read_flag", nullable = false)
    private boolean read;

    @Column(nullable = false)
    private Instant createdAt;

    private Instant readAt;

    protected UserNotificationEntity() {
    }

    public UserNotificationEntity(UUID id, NotificationEntity notification, UUID userId, boolean read, Instant createdAt, Instant readAt) {
        this.id = id;
        this.notification = notification;
        this.userId = userId;
        this.read = read;
        this.createdAt = createdAt;
        this.readAt = readAt;
    }

    public void markAsRead(Instant readAt) {
        this.read = true;
        this.readAt = readAt;
    }

    public UUID getId() {
        return id;
    }

    public NotificationEntity getNotification() {
        return notification;
    }

    public UUID getUserId() {
        return userId;
    }

    public boolean isRead() {
        return read;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getReadAt() {
        return readAt;
    }
}
