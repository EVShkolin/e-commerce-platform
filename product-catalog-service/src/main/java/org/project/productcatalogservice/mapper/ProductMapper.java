package org.project.productcatalogservice.mapper;


import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.project.productcatalogservice.dto.ProductDto;
import org.project.productcatalogservice.model.Product;
import org.springframework.data.domain.Page;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    ProductDto toDto(Product product);

    @Mapping(target = "updatedAt",ignore = true)
    @Mapping(target = "createdAt",ignore = true)
    Product toEntity(ProductDto productDto);

    List<ProductDto> toDtoList(List<Product> products);

    default Page<ProductDto> toDtoPage(Page<Product> productPage) {
        return productPage.map(this::toDto);
    }

    default void updateEntityFromDto(Product product, ProductDto productDto) {
        product.setName(productDto.getName());
        product.setDescription(productDto.getDescription());
        product.setCategory(productDto.getCategory());
        product.setPrice(productDto.getPrice());
        product.setRating(productDto.getRating());
    }

}
