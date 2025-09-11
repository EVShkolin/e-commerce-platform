package org.project.productcatalogservice.service;

import org.project.productcatalogservice.dto.ProductDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductService {

    Page<ProductDto> findAll(Pageable pageable);

    ProductDto findById(Long id);

    ProductDto save(ProductDto productDto);

    ProductDto update(Long id, ProductDto productDto);

    void delete(Long id);

}
