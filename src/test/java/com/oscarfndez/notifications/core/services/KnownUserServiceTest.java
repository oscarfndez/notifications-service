package com.oscarfndez.notifications.core.services;

import com.oscarfndez.notifications.core.events.UserLifecycleEvent;
import com.oscarfndez.notifications.persistence.entities.KnownUserEntity;
import com.oscarfndez.notifications.persistence.repositories.KnownUserRepository;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentCaptor.forClass;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class KnownUserServiceTest {

    private final KnownUserRepository knownUserRepository = mock(KnownUserRepository.class);
    private final KnownUserService service = new KnownUserService(knownUserRepository);

    @Test
    void handleCreatesKnownUserWhenUserCreatedEventArrives() {
        UUID userId = UUID.randomUUID();
        UserLifecycleEvent event = new UserLifecycleEvent("afterCreating", userId, "user@domain.com", "John", "Doe", "USER", Instant.now());

        when(knownUserRepository.findById(userId)).thenReturn(Optional.empty());

        service.handle(event);

        var captor = forClass(KnownUserEntity.class);
        verify(knownUserRepository).save(captor.capture());
        KnownUserEntity saved = captor.getValue();
        assertThat(saved.getId()).isEqualTo(userId);
        assertThat(saved.getEmail()).isEqualTo("user@domain.com");
        assertThat(saved.isActive()).isTrue();
    }

    @Test
    void handleDeactivatesKnownUserWhenUserDeletedEventArrives() {
        UUID userId = UUID.randomUUID();
        KnownUserEntity user = new KnownUserEntity(userId, "user@domain.com", "John", "Doe", "USER", true, Instant.now(), Instant.now());
        UserLifecycleEvent event = new UserLifecycleEvent("afterDeleting", userId, "user@domain.com", "John", "Doe", "USER", Instant.now());

        when(knownUserRepository.findById(userId)).thenReturn(Optional.of(user));

        service.handle(event);

        assertThat(user.isActive()).isFalse();
        verify(knownUserRepository).save(user);
    }
}
