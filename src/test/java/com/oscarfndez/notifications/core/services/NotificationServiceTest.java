package com.oscarfndez.notifications.core.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oscarfndez.notifications.core.events.InventoryEntityDeletedEvent;
import com.oscarfndez.notifications.persistence.entities.KnownUserEntity;
import com.oscarfndez.notifications.persistence.entities.NotificationEntity;
import com.oscarfndez.notifications.persistence.entities.UserNotificationEntity;
import com.oscarfndez.notifications.persistence.repositories.KnownUserRepository;
import com.oscarfndez.notifications.persistence.repositories.NotificationRepository;
import com.oscarfndez.notifications.persistence.repositories.UserNotificationRepository;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentCaptor.forClass;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class NotificationServiceTest {

    private final KnownUserRepository knownUserRepository = mock(KnownUserRepository.class);
    private final NotificationRepository notificationRepository = mock(NotificationRepository.class);
    private final UserNotificationRepository userNotificationRepository = mock(UserNotificationRepository.class);
    private final NotificationService service = new NotificationService(knownUserRepository, notificationRepository, userNotificationRepository, new ObjectMapper());

    @Test
    void createInventoryDeletedNotificationCreatesNotificationForEveryActiveKnownUser() {
        UUID userId = UUID.randomUUID();
        InventoryEntityDeletedEvent event = new InventoryEntityDeletedEvent("game.deleted", "GAME", UUID.randomUUID(), "Elden Ring", Instant.now());
        KnownUserEntity user = new KnownUserEntity(userId, "user@domain.com", "John", "Doe", "USER", true, Instant.now(), Instant.now());

        when(notificationRepository.save(any(NotificationEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(knownUserRepository.findByActiveTrue()).thenReturn(List.of(user));

        NotificationEntity notification = service.createInventoryDeletedNotification(event);

        assertThat(notification.getType()).isEqualTo("game.deleted");
        assertThat(notification.getTitleKey()).isEqualTo("notifications.inventory.gameDeleted.title");
        assertThat(notification.getMessageKey()).isEqualTo("notifications.inventory.gameDeleted.message");
        assertThat(notification.getParamsJson()).isEqualTo("{\"name\":\"Elden Ring\"}");
        assertThat(notification.getSourceEntityType()).isEqualTo("GAME");
        assertThat(notification.getSourceEntityName()).isEqualTo("Elden Ring");

        var captor = forClass(List.class);
        verify(userNotificationRepository).saveAll(captor.capture());
        List<UserNotificationEntity> savedUserNotifications = captor.getValue();
        assertThat(savedUserNotifications).hasSize(1);
        assertThat(savedUserNotifications.get(0).getUserId()).isEqualTo(userId);
        assertThat(savedUserNotifications.get(0).isRead()).isFalse();
    }
}
