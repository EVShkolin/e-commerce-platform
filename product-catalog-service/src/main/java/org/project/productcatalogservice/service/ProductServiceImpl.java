package org.project.productcatalogservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.project.productcatalogservice.dto.ProductDto;
import org.project.productcatalogservice.exceptionhandler.exception.ProductNotFoundException;
import org.project.productcatalogservice.mapper.ProductMapper;
import org.project.productcatalogservice.model.Product;
import org.project.productcatalogservice.repository.ProductRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductServiceImpl implements ProductService{

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    @Override
    @Transactional(readOnly = true)
    public Page<ProductDto> findAll(Pageable pageable) {
        log.debug("IN ProductServiceImpl findAll");
        return productMapper.toDtoPage(productRepository.findAll(pageable));
    }

    @Override
    @Transactional(readOnly = true)
    public ProductDto findById(Long id) {
        log.debug("IN ProductServiceImpl findById {}", id);
        return productRepository.findById(id)
                .map(productMapper::toDto)
                .orElseThrow(() -> new ProductNotFoundException(id));
    }

    @Override
    @Transactional
    public ProductDto save(ProductDto productDto) {
        log.info("IN ProductServiceImpl save {}", productDto);
        Product product = productMapper.toEntity(productDto);
        return productMapper.toDto(productRepository.save(product));
    }

    @Override
    @Transactional
    public ProductDto update(Long id, ProductDto productDto) {
        log.info("IN ProductServiceImpl update product {} with id {}", productDto, id);
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));

        productMapper.updateEntityFromDto(product, productDto);

        return productMapper.toDto(product);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        log.info("IN ProductServiceImpl delete {}", id);
        if (!productRepository.existsById(id))
            throw new ProductNotFoundException(id);

        productRepository.deleteById(id);
    }
}
