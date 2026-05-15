package com.oscarfndez.notifications.adapters.messaging;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oscarfndez.notifications.core.events.InventoryEntityDeletedEvent;
import com.oscarfndez.notifications.core.services.NotificationService;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentCaptor.forClass;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class InventoryEntityDeletedEventListenerTest {

    private final ObjectMapper objectMapper = new ObjectMapper().findAndRegisterModules();
    private final NotificationService notificationService = mock(NotificationService.class);
    private final InventoryEntityDeletedEventListener listener = new InventoryEntityDeletedEventListener(objectMapper, notificationService);

    @Test
    void onInventoryEntityDeletedEventParsesPayloadAndDelegatesToService() throws Exception {
        UUID entityId = UUID.randomUUID();
        String payload = objectMapper.writeValueAsString(new InventoryEntityDeletedEvent("game.deleted", "GAME", entityId, "Elden Ring", Instant.now()));

        listener.onInventoryEntityDeletedEvent(payload);

        var captor = forClass(InventoryEntityDeletedEvent.class);
        verify(notificationService).createInventoryDeletedNotification(captor.capture());
        assertThat(captor.getValue().entityId()).isEqualTo(entityId);
    }

    @Test
    void onInventoryEntityDeletedEventThrowsWhenPayloadIsInvalid() {
        assertThatThrownBy(() -> listener.onInventoryEntityDeletedEvent("not-json"))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
