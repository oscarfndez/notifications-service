package com.oscarfndez.notifications.adapters.messaging;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oscarfndez.notifications.core.events.UserLifecycleEvent;
import com.oscarfndez.notifications.core.services.KnownUserService;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentCaptor.forClass;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class UserLifecycleEventListenerTest {

    private final ObjectMapper objectMapper = new ObjectMapper().findAndRegisterModules();
    private final KnownUserService knownUserService = mock(KnownUserService.class);
    private final UserLifecycleEventListener listener = new UserLifecycleEventListener(objectMapper, knownUserService);

    @Test
    void onUserLifecycleEventParsesPayloadAndDelegatesToService() throws Exception {
        UUID userId = UUID.randomUUID();
        String payload = objectMapper.writeValueAsString(new UserLifecycleEvent("afterCreating", userId, "user@domain.com", "John", "Doe", "USER", Instant.now()));

        listener.onUserLifecycleEvent(payload);

        var captor = forClass(UserLifecycleEvent.class);
        verify(knownUserService).handle(captor.capture());
        assertThat(captor.getValue().userId()).isEqualTo(userId);
    }

    @Test
    void onUserLifecycleEventThrowsWhenPayloadIsInvalid() {
        assertThatThrownBy(() -> listener.onUserLifecycleEvent("not-json"))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
