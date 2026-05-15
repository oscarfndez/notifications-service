package com.oscarfndez.notifications.adapters.rest;

import com.oscarfndez.notifications.core.services.NotificationService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @GetMapping
    public Page<NotificationDto> retrieveForUser(
            @RequestParam UUID userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        return notificationService.retrieveForUser(userId, pageable).map(NotificationDto::from);
    }

    @GetMapping("/unread-count")
    public Map<String, Long> countUnread(@RequestParam UUID userId) {
        return Map.of("unread", notificationService.countUnread(userId));
    }

    @PatchMapping("/{id}/read")
    public ResponseEntity<NotificationDto> markAsRead(@PathVariable UUID id, @RequestParam UUID userId) {
        return ResponseEntity.ok(NotificationDto.from(notificationService.markAsRead(id, userId)));
    }
}
