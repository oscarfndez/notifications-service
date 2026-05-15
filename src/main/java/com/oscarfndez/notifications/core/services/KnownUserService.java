package com.oscarfndez.notifications.core.services;

import com.oscarfndez.notifications.core.events.UserLifecycleEvent;
import com.oscarfndez.notifications.persistence.entities.KnownUserEntity;
import com.oscarfndez.notifications.persistence.repositories.KnownUserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
public class KnownUserService {

    private final KnownUserRepository knownUserRepository;

    public KnownUserService(KnownUserRepository knownUserRepository) {
        this.knownUserRepository = knownUserRepository;
    }

    @Transactional
    public void handle(UserLifecycleEvent event) {
        if ("afterDeleting".equals(event.eventType())) {
            deactivate(event);
            return;
        }

        if ("afterCreating".equals(event.eventType()) || "afterUpdating".equals(event.eventType())) {
            upsert(event);
        }
    }

    private void upsert(UserLifecycleEvent event) {
        Instant now = Instant.now();
        KnownUserEntity user = knownUserRepository.findById(event.userId())
                .orElseGet(() -> new KnownUserEntity(
                        event.userId(),
                        event.email(),
                        event.firstName(),
                        event.lastName(),
                        event.role(),
                        true,
                        now,
                        now
                ));

        user.update(event.email(), event.firstName(), event.lastName(), event.role(), true, now);
        knownUserRepository.save(user);
    }

    private void deactivate(UserLifecycleEvent event) {
        knownUserRepository.findById(event.userId()).ifPresent(user -> {
            user.update(user.getEmail(), user.getFirstName(), user.getLastName(), user.getRole(), false, Instant.now());
            knownUserRepository.save(user);
        });
    }
}
