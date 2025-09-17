package org.project.shoppingcartservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "cart")
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cart_id")
    private Long id;

    @Column(name = "user_id")
    private String userId;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CartItem> items;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private Instant updatedAt;

    public Cart(String userId) {
        this.userId = userId;
        this.items = new ArrayList<>();
    }

    public List<Long> getProductIds() {
        return items.stream()
                .map(CartItem::getProductId)
                .toList();
    }

    public void addItem(CartItem cartItem) {
        items.add(cartItem);
    }

    public void removeItem(Long productId) {
        items.stream()
                .filter(item -> item.getProductId().equals(productId))
                .findFirst()
                .ifPresent(item -> {
                    items.remove(item);
                    item.setCart(null);
                });
    }

    public void clear() {
        items.clear();
    }
}
