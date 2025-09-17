package org.project.shoppingcartservice.service;

import org.project.shoppingcartservice.dto.CartDto;
import org.project.shoppingcartservice.dto.CartItemDto;

public interface CartService {

    CartDto findByUserId(String userId);

    void addItemByUserId(String userId, CartItemDto cartItemDto);

    void deleteItemByUserId(String userId, Long productId);

    void clearCart(String userId);
}
