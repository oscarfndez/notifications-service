package com.oscarfndez.notifications.adapters.messaging;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.oscarfndez.notifications.core.events.UserLifecycleEvent;
import com.oscarfndez.notifications.core.services.KnownUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
public class UserLifecycleEventListener {

    private static final Logger log = LoggerFactory.getLogger(UserLifecycleEventListener.class);

    private final ObjectMapper objectMapper;
    private final KnownUserService knownUserService;

    public UserLifecycleEventListener(ObjectMapper objectMapper, KnownUserService knownUserService) {
        this.objectMapper = objectMapper;
        this.knownUserService = knownUserService;
    }

    @JmsListener(
            destination = "${user.events.topic:games-collection.user-events}",
            subscription = "${user.events.subscription:notifications-service-user-events}"
    )
    public void onUserLifecycleEvent(String payload) {
        try {
            UserLifecycleEvent event = objectMapper.readValue(payload, UserLifecycleEvent.class);
            knownUserService.handle(event);
            log.info("Processed user lifecycle event type={} userId={}", event.eventType(), event.userId());
        } catch (JsonProcessingException exception) {
            log.error("Unable to parse user lifecycle event payload={} error={}", payload, exception.getOriginalMessage());
            throw new IllegalArgumentException("Invalid user lifecycle event payload.", exception);
        }
    }
}
