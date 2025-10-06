package org.project.paymentservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "payment")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "payment_id")
    private UUID id;

    @Column(name = "order_id")
    private Long orderId;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "stripe_payment_intent_id")
    private String stripePaymentIntentId;

    @Column(name = "total_price")
    private BigDecimal totalPrice;

    @Column(name = "status")
    @Enumerated(value = EnumType.STRING)
    private PaymentStatus status;

    @Column(name = "updated_at")
    @UpdateTimestamp
    private Instant updatedAt;

    @Column(name = "created_at")
    @CreationTimestamp
    private Instant createdAt;

    public Payment(Long orderId, String userId, BigDecimal totalPrice, PaymentStatus status) {
        this.orderId = orderId;
        this.userId = userId;
        this.totalPrice = totalPrice;
        this.status = status;
    }
}
