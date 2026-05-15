package com.oscarfndez.notifications.core.services;

import com.oscarfndez.notifications.core.events.InventoryEntityDeletedEvent;
import com.oscarfndez.notifications.persistence.entities.KnownUserEntity;
import com.oscarfndez.notifications.persistence.entities.NotificationEntity;
import com.oscarfndez.notifications.persistence.entities.UserNotificationEntity;
import com.oscarfndez.notifications.persistence.repositories.KnownUserRepository;
import com.oscarfndez.notifications.persistence.repositories.NotificationRepository;
import com.oscarfndez.notifications.persistence.repositories.UserNotificationRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

@Service
public class NotificationService {

    private final KnownUserRepository knownUserRepository;
    private final NotificationRepository notificationRepository;
    private final UserNotificationRepository userNotificationRepository;

    public NotificationService(
            KnownUserRepository knownUserRepository,
            NotificationRepository notificationRepository,
            UserNotificationRepository userNotificationRepository
    ) {
        this.knownUserRepository = knownUserRepository;
        this.notificationRepository = notificationRepository;
        this.userNotificationRepository = userNotificationRepository;
    }

    @Transactional
    public NotificationEntity createInventoryDeletedNotification(InventoryEntityDeletedEvent event) {
        Instant now = Instant.now();
        NotificationEntity notification = notificationRepository.save(new NotificationEntity(
                UUID.randomUUID(),
                event.eventType(),
                titleFor(event.entityType()),
                messageFor(event),
                "inventory",
                event.entityType(),
                event.entityId(),
                event.entityName(),
                event.occurredAt(),
                now
        ));

        List<UserNotificationEntity> userNotifications = knownUserRepository.findByActiveTrue().stream()
                .map(KnownUserEntity::getId)
                .map(userId -> new UserNotificationEntity(UUID.randomUUID(), notification, userId, false, now, null))
                .toList();

        userNotificationRepository.saveAll(userNotifications);
        return notification;
    }

    @Transactional(readOnly = true)
    public Page<UserNotificationEntity> retrieveForUser(UUID userId, Pageable pageable) {
        return userNotificationRepository.findByUserIdOrderByCreatedAtDesc(userId, pageable);
    }

    @Transactional(readOnly = true)
    public long countUnread(UUID userId) {
        return userNotificationRepository.countByUserIdAndReadFalse(userId);
    }

    @Transactional
    public UserNotificationEntity markAsRead(UUID userNotificationId, UUID userId) {
        UserNotificationEntity userNotification = userNotificationRepository.findByIdAndUserId(userNotificationId, userId)
                .orElseThrow(() -> new IllegalArgumentException("Notification not found for user."));
        userNotification.markAsRead(Instant.now());
        return userNotificationRepository.save(userNotification);
    }

    private String titleFor(String entityType) {
        return switch (entityType.toUpperCase(Locale.ROOT)) {
            case "GAME" -> "Game deleted";
            case "PLATFORM" -> "Platform deleted";
            case "STUDIO" -> "Studio deleted";
            default -> "Inventory item deleted";
        };
    }

    private String messageFor(InventoryEntityDeletedEvent event) {
        String entityName = event.entityName() == null || event.entityName().isBlank()
                ? "An inventory item"
                : event.entityName();
        return entityName + " has been deleted from the inventory.";
    }
}
