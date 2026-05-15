package com.oscarfndez.notifications.persistence.repositories;

import com.oscarfndez.notifications.persistence.entities.UserNotificationEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserNotificationRepository extends JpaRepository<UserNotificationEntity, UUID> {

    @EntityGraph(attributePaths = "notification")
    Page<UserNotificationEntity> findByUserIdOrderByCreatedAtDesc(UUID userId, Pageable pageable);

    long countByUserIdAndReadFalse(UUID userId);

    @EntityGraph(attributePaths = "notification")
    Optional<UserNotificationEntity> findByIdAndUserId(UUID id, UUID userId);
}
