package com.oscarfndez.notifications.persistence.repositories;

import com.oscarfndez.notifications.persistence.entities.NotificationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface NotificationRepository extends JpaRepository<NotificationEntity, UUID> {
}
