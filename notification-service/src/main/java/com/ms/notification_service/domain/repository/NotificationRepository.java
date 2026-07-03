package com.ms.notification_service.domain.repository;


import com.ms.notification_service.domain.model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
}
