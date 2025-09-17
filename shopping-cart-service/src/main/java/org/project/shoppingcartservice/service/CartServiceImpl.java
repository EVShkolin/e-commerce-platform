package org.project.shoppingcartservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.project.shoppingcartservice.client.ProductClient;
import org.project.shoppingcartservice.dto.CartDto;
import org.project.shoppingcartservice.dto.CartItemDto;
import org.project.shoppingcartservice.mapper.CartItemMapper;
import org.project.shoppingcartservice.model.Cart;
import org.project.shoppingcartservice.model.CartItem;
import org.project.shoppingcartservice.repository.CartItemRepository;
import org.project.shoppingcartservice.repository.CartRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductClient productClient;
    private final CartItemMapper cartItemMapper;

    @Override
    public CartDto findByUserId(String userId) {
        Cart cart = cartRepository.findCartByUserId(userId);
        if (cart == null) cart = cartRepository.save(new Cart(userId));
        if (cart.getItems().isEmpty()) return new CartDto(userId);

        List<CartItemDto> cartItemDtoList = cartItemMapper.convertToItemDtos(
                                    productClient.findProductsByIds(cart.getProductIds()),
                                    cart.getItems());

        CartDto cartDto = new CartDto(userId);
        cartDto.setItems(cartItemDtoList);

        return cartDto;
    }

    @Override
    @Transactional
    public void addItemByUserId(String userId, CartItemDto cartItemDto) {
        log.info("Item {} added to cart with user id {}", cartItemDto.getProductId(), userId);

        Cart cart = cartRepository.findCartByUserId(userId);
        if (cart == null) cart = cartRepository.save(new Cart(userId));

        CartItem cartItem = cartItemMapper.convertToCartItem(cartItemDto);
        cart.addItem(cartItem);
        cartItem.setCart(cart);
        cartItemRepository.save(cartItem);
    }

    @Override
    @Transactional
    public void deleteItemByUserId(String userId, Long productId) {
        log.info("Item {} deleted from {} cart", productId, userId);
        Cart cart = cartRepository.findCartByUserId(userId);
        cart.removeItem(productId);
    }

    @Override
    @Transactional
    public void clearCart(String userId) {
        Cart cart = cartRepository.findCartByUserId(userId);

        if (cart != null)
            cart.clear();
    }
}
