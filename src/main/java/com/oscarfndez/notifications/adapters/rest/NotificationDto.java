package com.oscarfndez.notifications.adapters.rest;

import com.oscarfndez.notifications.persistence.entities.NotificationEntity;
import com.oscarfndez.notifications.persistence.entities.UserNotificationEntity;

import java.time.Instant;
import java.util.UUID;

public record NotificationDto(
        UUID id,
        UUID notificationId,
        String type,
        String titleKey,
        String messageKey,
        String paramsJson,
        String sourceService,
        String sourceEntityType,
        UUID sourceEntityId,
        String sourceEntityName,
        boolean read,
        Instant createdAt,
        Instant readAt
) {

    public static NotificationDto from(UserNotificationEntity entity) {
        NotificationEntity notification = entity.getNotification();
        return new NotificationDto(
                entity.getId(),
                notification.getId(),
                notification.getType(),
                notification.getTitleKey(),
                notification.getMessageKey(),
                notification.getParamsJson(),
                notification.getSourceService(),
                notification.getSourceEntityType(),
                notification.getSourceEntityId(),
                notification.getSourceEntityName(),
                entity.isRead(),
                entity.getCreatedAt(),
                entity.getReadAt()
        );
    }
}
