package com.ms.notification_service.domain.model;


import com.ms.notification_service.domain.enums.NotificationStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Table(name = "notifications")
@Entity
@NoArgsConstructor
@Getter
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long customerId;
    private Long orderId;
    private String type;

    @Column(length = 1000)
    private String message;

    private NotificationStatus status;

    private Instant createdAt;

    public static Notification orderConfirmed(Long orderId, Long customerId) {
        Notification notification = new Notification();
        notification.orderId = orderId;
        notification.customerId = customerId;
        notification.type = "ORDER_CONFIRMED";
        notification.message = "Order confirmed. orderId=" + orderId;
        notification.status = NotificationStatus.CREATED;
        notification.createdAt = Instant.now();
        return notification;
    }
}
