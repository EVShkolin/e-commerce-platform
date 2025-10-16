package org.project.notificationservice.service;

import com.resend.Resend;
import com.resend.core.exception.ResendException;
import com.resend.services.emails.model.CreateEmailOptions;
import com.resend.services.emails.model.CreateEmailResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.project.notificationservice.event.OrderConfirmedEvent;
import org.project.notificationservice.event.UserRegisteredEvent;
import org.project.notificationservice.model.Notification;
import org.project.notificationservice.model.NotificationType;
import org.project.notificationservice.repository.NotificationRepository;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;
    private final Resend resend;

    @Override
    @KafkaListener(topics = "${kafka.topics.users-registered}")
    public void sendUserRegisteredNotification(UserRegisteredEvent event) {
        String text = "Thank you for signing up, " + event.getUsername();
        sendNotification(event.getUserId(),
                NotificationType.USER_REGISTERED,
                event.getEmail(),
                "Welcome message",
                text);
    }

    @Override
    @KafkaListener(topics = "${kafka.topics.orders-confirmed}")
    public void sendOrderPaidNotification(OrderConfirmedEvent event) {
        String text = "Thank you for your purchase, order id: " + event.getOrderId();
        sendNotification(event.getUserId(),
                NotificationType.ORDER_PAID,
                event.getEmail(),
                "Order paid",
                text);
    }

    private void sendNotification(String userId, NotificationType type, String email, String subject, String text) {
        Notification notification = notificationRepository.save(
                new Notification(userId, email, type)
        );
        CreateEmailOptions params = CreateEmailOptions.builder()
                .from("onboarding@resend.dev")
                .to(email)
                .subject(subject)
                .text(text)
                .build();

        try {
            CreateEmailResponse data = resend.emails().send(params);
            notification.markAsSent(data.getId());
            notificationRepository.save(notification);
            log.info("Sent {} to {}", subject, email);
        } catch (ResendException ex) {
            notification.markAsFailed();
            notificationRepository.save(notification);
            log.info("Could not send {} to {}", subject, email);
        }
    }
}
