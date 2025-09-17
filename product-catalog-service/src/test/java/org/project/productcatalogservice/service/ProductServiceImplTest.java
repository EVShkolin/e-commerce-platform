package org.project.productcatalogservice.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.project.productcatalogservice.dto.ProductDto;
import org.project.productcatalogservice.exceptionhandler.exception.ProductNotFoundException;
import org.project.productcatalogservice.mapper.ProductMapper;
import org.project.productcatalogservice.model.Product;
import org.project.productcatalogservice.repository.ProductRepository;
import org.project.productcatalogservice.util.DataUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class ProductServiceImplTest {

    @Mock
    ProductRepository productRepository;

    @Mock
    ProductMapper productMapper;

    @InjectMocks
    ProductServiceImpl productService;

    @Test
    @DisplayName("Find all test")
    public void findAll_shouldReturnPage() {
        // given
        Pageable pageable = Pageable.ofSize(10);
        Page<Product> productPage = new PageImpl<>(List.of(
                DataUtils.getTestProduct1(),
                DataUtils.getTestProduct2(),
                DataUtils.getTestProduct3()
        ));
        Page<ProductDto> productPageDto = new PageImpl<>(List.of(
                DataUtils.getTestProductDto1(),
                DataUtils.getTestProductDto2(),
                DataUtils.getTestProductDto3()
        ));

        BDDMockito.given(productRepository.findAll(any(Pageable.class))).willReturn(productPage);
        BDDMockito.given(productMapper.toDtoPage(productPage)).willReturn(productPageDto);

        // when
        Page<ProductDto> result = productService.findAll(pageable);

        // then
        assertThat(result.getContent())
                .isNotEmpty()
                .hasSize(3)
                .containsExactlyElementsOf(productPageDto.getContent());
        verify(productRepository).findAll(pageable);
        verify(productMapper).toDtoPage(productPage);
    }

    @Test
    @DisplayName("Find existing product by id test")
    public void findById_whenExists_shouldReturnDto() {
        // given
        BDDMockito.given(productRepository.findById(anyLong()))
                .willReturn(Optional.of(DataUtils.getTestProduct1()));
        BDDMockito.given(productMapper.toDto(any(Product.class)))
                .willReturn(DataUtils.getTestProductDto1());

        // when
        ProductDto result = productService.findById(1L);

        // then
        assertThat(result).isNotNull();
        assertEquals(result, DataUtils.getTestProductDto1());
    }

    @Test
    @DisplayName("Find not existing product by id test")
    public void findById_whenNotExists_shouldThrow()  {
        // given
        BDDMockito.given(productRepository.findById(anyLong())).willReturn(Optional.empty());

        // when
        assertThrows(ProductNotFoundException.class, () -> productService.findById(1L));

        // then
        verify(productRepository).findById(1L);
    }

    @Test
    @DisplayName("Save product test")
    public void save_shouldReturnSavedDto() {
        // given
        ProductDto productDtoToSave = DataUtils.getTestProductDto1();

        BDDMockito.given(productRepository.save(any(Product.class)))
                .willReturn(DataUtils.getTestProduct1());
        BDDMockito.given(productMapper.toDto(any(Product.class)))
                .willReturn(DataUtils.getTestProductDto1());
        BDDMockito.given(productMapper.toEntity(any(ProductDto.class)))
                .willReturn(DataUtils.getTestProduct1());

        // when
        ProductDto result = productService.save(productDtoToSave);

        // then
        assertThat(result).isNotNull();
        assertEquals(result, DataUtils.getTestProductDto1());
    }

    @Test
    @DisplayName("Update existing product test")
    public void update_whenExists_shouldReturnDto() {
        // given
        ProductDto productDtoToUpdate = DataUtils.getTestProductDto1();

        BDDMockito.given(productRepository.findById(anyLong()))
                .willReturn(Optional.of(DataUtils.getTestProduct1()));
        BDDMockito.given(productMapper.toDto(any(Product.class)))
                .willReturn(DataUtils.getTestProductDto1());
        BDDMockito.willDoNothing().given(productMapper).updateEntityFromDto(any(Product.class), any(ProductDto.class));

        // when
        ProductDto result = productService.update(1L, productDtoToUpdate);

        // then
        assertThat(result).isNotNull();
        assertEquals(result, productDtoToUpdate);
    }

    @Test
    @DisplayName("Update not existing product test")
    public void update_whenNotExists_shouldThrow() {
        // given
        BDDMockito.given(productRepository.findById(anyLong())).willReturn(Optional.empty());

        // when
        assertThrows(
                ProductNotFoundException.class, () -> productService.update(9L, DataUtils.getTestProductDto1()));
        // then
    }

    @Test
    @DisplayName("Delete existing product test")
    public void delete_whenExists() {
        // given
        BDDMockito.given(productRepository.existsById(anyLong())).willReturn(true);

        // when
        productService.delete(1L);

        // then
        verify(productRepository).deleteById(1L);
    }

    @Test
    @DisplayName("Delete not existing product")
    public void delete_whenNotExists_shouldThrow() {
        // given
        BDDMockito.given(productRepository.existsById(anyLong())).willReturn(false);

        // when
        assertThrows(ProductNotFoundException.class, () -> productService.delete(1L));

        // then
        verify(productRepository, never()).deleteById(1L);
    }
}
