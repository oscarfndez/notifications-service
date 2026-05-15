package com.oscarfndez.notifications.core.events;

import java.time.Instant;
import java.util.UUID;

public record InventoryEntityDeletedEvent(
        String eventType,
        String entityType,
        UUID entityId,
        String entityName,
        Instant occurredAt
) {
}
