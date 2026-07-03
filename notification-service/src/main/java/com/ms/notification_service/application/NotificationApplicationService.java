package com.ms.notification_service.application;


import com.ms.common.contracts.order.OrderConfirmedEventPayload;
import com.ms.common.messaging.MessageEnvelope;
import com.ms.notification_service.domain.model.Notification;
import com.ms.notification_service.domain.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationApplicationService {

    private final NotificationRepository notificationRepository;

    @Transactional
    public void createOrderConfirmedNotification(MessageEnvelope<OrderConfirmedEventPayload> event) {
        OrderConfirmedEventPayload payload = event.getPayload();
        Notification notification = Notification.orderConfirmed(payload.orderId(), payload.customerId());

        notificationRepository.save(notification);
        log.info("Notification created for confirmed order. orderId={}, customerId={}, correlationId={}",
                payload.orderId(),
                payload.customerId(),
                event.getCorrelationId()
        );
    }
}
