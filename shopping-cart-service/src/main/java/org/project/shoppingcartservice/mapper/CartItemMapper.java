package org.project.shoppingcartservice.mapper;

import org.project.shoppingcartservice.dto.CartItemDto;
import org.project.shoppingcartservice.dto.ExternalProductDto;
import org.project.shoppingcartservice.model.CartItem;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class CartItemMapper {

    public List<CartItemDto> convertToItemDtos(List<ExternalProductDto> products, List<CartItem> cartItems) {
        Map<Long, ExternalProductDto> productMap = products.stream()
                .collect(Collectors.toMap(ExternalProductDto::getId, productDto -> productDto));

        return cartItems.stream()
                .filter(item -> productMap.containsKey(item.getProductId()))
                .map(item -> {
                    ExternalProductDto product = productMap.get(item.getProductId());
                    return new CartItemDto(item.getProductId(),
                            product.getName(),
                            product.getPrice(),
                            item.getQuantity()
                    );
                })
                .toList();
    }

    public CartItem convertToCartItem(CartItemDto cartItemDto) {
        return new CartItem(cartItemDto.getProductId(), cartItemDto.getQuantity());
    }

}
