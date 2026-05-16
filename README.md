# Notifications Service

Spring Boot microservice responsible for creating user notifications in the Games Collection platform.

It consumes domain events from ActiveMQ and stores notifications in PostgreSQL. The first supported notification type is an inventory deletion notification, created when a game, platform, or studio is deleted in `inventory-service`.

## Responsibilities

- Keep a local projection of known users from `users-service` lifecycle events.
- Consume inventory deletion events from `inventory-service`.
- Create one notification per deleted inventory entity.
- Create one user-notification row per active known user.
- Track whether a user has read a notification.
- Store notification translation keys and parameters instead of final rendered text.
- Expose basic endpoints to list and mark notifications as read.

## Architecture Notes

This service is intentionally event-driven. It does not read tables from `users-service` or `inventory-service`.

Current trade-off: the `known_user` projection is populated from future user lifecycle events. Existing users created before this service starts will need a bootstrap/backfill step later, or a manual seed, before global notifications can be fanned out to them.

Notification text is intentionally not stored as rendered text. The database stores `titleKey`, `messageKey`, and `paramsJson`; the frontend is responsible for translating and interpolating the notification according to the selected UI language.

## Requirements

- Java 17+
- Maven 3.9+
- PostgreSQL
- ActiveMQ

## Configuration

```bash
SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/notifications
SPRING_DATASOURCE_USERNAME=oscar
SPRING_DATASOURCE_PASSWORD=password
SPRING_ACTIVEMQ_BROKER_URL=tcp://localhost:61616
SPRING_ACTIVEMQ_USER=admin
SPRING_ACTIVEMQ_PASSWORD=admin
USER_EVENTS_TOPIC=games-collection.user-events
USER_EVENTS_SUBSCRIPTION=notifications-service-user-events
INVENTORY_EVENTS_TOPIC=games-collection.inventory-events
INVENTORY_EVENTS_SUBSCRIPTION=notifications-service-inventory-events
JMS_CLIENT_ID=notifications-service
```

## Run Locally

```bash
mvn spring-boot:run
```

## Test

```bash
mvn test
```

## Endpoints

### List User Notifications

```http
GET /api/notifications?userId={uuid}&page=0&size=10
```

Example item:

```json
{
  "id": "11111111-1111-1111-1111-111111111111",
  "notificationId": "22222222-2222-2222-2222-222222222222",
  "type": "game.deleted",
  "titleKey": "notifications.inventory.gameDeleted.title",
  "messageKey": "notifications.inventory.gameDeleted.message",
  "paramsJson": "{\"name\":\"Elden Ring\"}",
  "sourceService": "inventory",
  "sourceEntityType": "GAME",
  "sourceEntityId": "00000000-0000-0000-0000-000000000000",
  "sourceEntityName": "Elden Ring",
  "read": false,
  "createdAt": "2026-05-15T12:00:00Z",
  "readAt": null
}
```

### Count Unread Notifications

```http
GET /api/notifications/unread-count?userId={uuid}
```

### Mark Notification As Read

```http
PATCH /api/notifications/{userNotificationId}/read?userId={uuid}
```

## Consumed Events

### User Lifecycle Events

Topic: `games-collection.user-events`

```json
{
  "eventType": "afterCreating",
  "userId": "00000000-0000-0000-0000-000000000000",
  "email": "user@domain.com",
  "firstName": "John",
  "lastName": "Doe",
  "role": "USER",
  "occurredAt": "2026-05-15T12:00:00Z"
}
```

Supported event types:

- `afterCreating`
- `afterUpdating`
- `afterDeleting`

### Inventory Deletion Events

Topic: `games-collection.inventory-events`

```json
{
  "eventType": "game.deleted",
  "entityType": "GAME",
  "entityId": "00000000-0000-0000-0000-000000000000",
  "entityName": "Elden Ring",
  "occurredAt": "2026-05-15T12:00:00Z"
}
```

Supported entity types:

- `GAME`
- `PLATFORM`
- `STUDIO`

## Observability

Prometheus metrics are exposed at:

```http
GET /actuator/prometheus
```
