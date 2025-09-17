package org.project.shoppingcartservice.controller;

import lombok.RequiredArgsConstructor;
import org.project.shoppingcartservice.dto.CartDto;
import org.project.shoppingcartservice.dto.CartItemDto;
import org.project.shoppingcartservice.service.CartService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @GetMapping("/{userId}")
    public ResponseEntity<CartDto> getCartByUserId(@PathVariable String userId) {
        return new ResponseEntity<>(cartService.findByUserId(userId), HttpStatus.OK);
    }

    @PostMapping("/{userId}/items")
    public ResponseEntity<Void> addItemToCart(@PathVariable String userId, @RequestBody CartItemDto cartItemDto) {
        cartService.addItemByUserId(userId, cartItemDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/{userId}/items/{productId}")
    public ResponseEntity<Void> deleteItemFromCart(@PathVariable String userId, @PathVariable Long productId) {
        cartService.deleteItemByUserId(userId, productId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> clearCart(@PathVariable String userId) {
        cartService.clearCart(userId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
