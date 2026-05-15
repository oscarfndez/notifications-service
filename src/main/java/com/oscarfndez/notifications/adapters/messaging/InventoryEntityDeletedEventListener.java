package com.oscarfndez.notifications.adapters.messaging;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.oscarfndez.notifications.core.events.InventoryEntityDeletedEvent;
import com.oscarfndez.notifications.core.services.NotificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
public class InventoryEntityDeletedEventListener {

    private static final Logger log = LoggerFactory.getLogger(InventoryEntityDeletedEventListener.class);

    private final ObjectMapper objectMapper;
    private final NotificationService notificationService;

    public InventoryEntityDeletedEventListener(ObjectMapper objectMapper, NotificationService notificationService) {
        this.objectMapper = objectMapper;
        this.notificationService = notificationService;
    }

    @JmsListener(
            destination = "${inventory.events.topic:games-collection.inventory-events}",
            subscription = "${inventory.events.subscription:notifications-service-inventory-events}"
    )
    public void onInventoryEntityDeletedEvent(String payload) {
        try {
            InventoryEntityDeletedEvent event = objectMapper.readValue(payload, InventoryEntityDeletedEvent.class);
            notificationService.createInventoryDeletedNotification(event);
            log.info("Processed inventory deletion event type={} entityType={} entityId={}", event.eventType(), event.entityType(), event.entityId());
        } catch (JsonProcessingException exception) {
            log.error("Unable to parse inventory deletion event payload={} error={}", payload, exception.getOriginalMessage());
            throw new IllegalArgumentException("Invalid inventory deletion event payload.", exception);
        }
    }
}
