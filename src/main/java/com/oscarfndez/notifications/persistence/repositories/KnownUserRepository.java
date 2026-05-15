package com.oscarfndez.notifications.persistence.repositories;

import com.oscarfndez.notifications.persistence.entities.KnownUserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface KnownUserRepository extends JpaRepository<KnownUserEntity, UUID> {

    List<KnownUserEntity> findByActiveTrue();
}
