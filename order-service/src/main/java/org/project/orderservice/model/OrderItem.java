package org.project.orderservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_item_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    @Column(name = "product_id")
    private Long productId;

    @Column(name = "product_name")
    private String productName;

    @Column(name = "price_snapshot")
    private BigDecimal priceSnapshot;

    @Column(name = "quantity")
    private Integer quantity;

    public OrderItem(Long productId, String productName, BigDecimal priceSnapshot, Integer quantity) {
        this.productId = productId;
        this.productName = productName;
        this.priceSnapshot = priceSnapshot;
        this.quantity = quantity;
    }

}
