package org.project.notificationservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "notification")
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notification_id")
    private Long id;

    @Column(name = "resend_id")
    private String resendId;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "email")
    private String email;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "type")
    private NotificationType type;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "status")
    private NotificationStatus status;

    @Column(name = "sent_at")
    private Instant sentAt;

    public Notification(String userId, String email, NotificationType type) {
        this.userId = userId;
        this.email = email;
        this.type = type;
        this.status = NotificationStatus.PENDING;
    }

    public void markAsSent(String resendId) {
        this.resendId = resendId;
        this.status = NotificationStatus.SENT;
        this.sentAt = Instant.now();
    }

    public void markAsFailed() {
        this.status = NotificationStatus.FAILED;
    }
}
