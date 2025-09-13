package org.project.productcatalogservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.project.productcatalogservice.dto.ProductDto;
import org.project.productcatalogservice.exceptionhandler.exception.ProductNotFoundException;
import org.project.productcatalogservice.service.ProductService;
import org.project.productcatalogservice.util.DataUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
public class ProductControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockitoBean
    ProductService productService;

    @Test
    @DisplayName("Get all products test")
    public void getAllProducts_shouldReturnPage() throws Exception {
        // given
        Page<ProductDto> page = new PageImpl<>(List.of(
                DataUtils.getTestProductDto1(),
                DataUtils.getTestProductDto2(),
                DataUtils.getTestProductDto3()
        ));
        BDDMockito.given(productService.findAll(any(Pageable.class))).willReturn(page);

        // when and then
        mockMvc.perform(get("/api/v1/products")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(3)))
                .andExpect(jsonPath("$.content[0].id", is(1)))
                .andExpect(jsonPath("$.content[1].id", is(2)))
                .andExpect(jsonPath("$.content[2].id", is(3)));
        verify(productService).findAll(any(Pageable.class));
    }

    @Test
    @DisplayName("Get product by id success test")
    public void getProductById_whenExists_shouldReturnProduct() throws Exception {
        // given
        BDDMockito.given(productService.findById(anyLong())).willReturn(DataUtils.getTestProductDto1());

        // when and then
        mockMvc.perform(get("/api/v1/products/" + 1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Product 1")))
                .andExpect(jsonPath("$.description", is("Test product 1")))
                .andExpect(jsonPath("$.price", is(10)))
                .andExpect(jsonPath("$.category", is("Category1")))
                .andExpect(jsonPath("$.rating", is(4.5)));
        verify(productService).findById(1L);
    }

    @Test
    @DisplayName("Get product by id not found test")
    public void getProductByID_whenNotExists_shouldReturnNotFound() throws Exception {
        // given
        BDDMockito.given(productService.findById(1L))
                .willThrow(new ProductNotFoundException(1L));

        // when and then
        mockMvc.perform(get("/api/v1/products/" + 1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$", is("Could not find product with id " + 1)));
        verify(productService).findById(1L);
    }

    @Test
    @DisplayName("Save product success test")
    public void saveProduct_withValidData_shouldReturnCreated() throws Exception {
        // given
        ProductDto productDto = DataUtils.getTestProductDto1();
        productDto.setId(null);

        BDDMockito.given(productService.save(any(ProductDto.class)))
                .willReturn(productDto);

        // when and then
        mockMvc.perform(post("/api/v1/products")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(productDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", is("Product 1")))
                .andExpect(jsonPath("$.description", is("Test product 1")))
                .andExpect(jsonPath("$.price", is(10)))
                .andExpect(jsonPath("$.category", is("Category1")))
                .andExpect(jsonPath("$.rating", is(4.5)));
        verify(productService).save(any(ProductDto.class));
    }

    @Test
    @DisplayName("Save product invalid data test")
    public void saveProduct_withInvalidData_shouldReturnBadRequest() throws Exception {
        // given
        ProductDto productDto = DataUtils.getTestProductDto1();
        productDto.setId(null);
        productDto.setPrice(BigDecimal.valueOf(-10));

        // when and then
        mockMvc.perform(post("/api/v1/products")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(productDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.price", is("Price cannot be negative")));
        verify(productService, never()).save(any(ProductDto.class));
    }

    @Test
    @DisplayName("Update product success test")
    public void updateProduct_whenExists_shouldReturnUpdated() throws Exception {
        // given
        ProductDto productDto = DataUtils.getTestProductDto1();
        BDDMockito.given(productService.update(anyLong(), any(ProductDto.class)))
                .willReturn(DataUtils.getTestProductDto1());

        // when and then
        mockMvc.perform(put("/api/v1/products/" + 1)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(productDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Product 1")))
                .andExpect(jsonPath("$.description", is("Test product 1")))
                .andExpect(jsonPath("$.price", is(10)))
                .andExpect(jsonPath("$.category", is("Category1")))
                .andExpect(jsonPath("$.rating", is(4.5)));
        verify(productService).update(1L, productDto);
    }

    @Test
    @DisplayName("Update product not found test")
    public void updateProduct_whenNotExists_shouldReturnNotFound() throws Exception {
        // given
        BDDMockito.given(productService.update(anyLong(), any(ProductDto.class)))
                .willThrow(new ProductNotFoundException(1L));

        // when and then
        mockMvc.perform(put("/api/v1/products/" + 1)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(DataUtils.getTestProductDto1())))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$", is("Could not find product with id " + 1)));
        verify(productService).update(anyLong(), any(ProductDto.class));
    }

    @Test
    @DisplayName("Delete product success test")
    public void deleteProduct_whenExists() throws Exception {
        // given
        BDDMockito.doNothing().when(productService).delete(anyLong());

        // when and then
        mockMvc.perform(delete("/api/v1/products/" + 1)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
        verify(productService).delete(1L);
    }

    @Test
    public void deleteProduct_whenNotExists_returnNotFound() throws Exception {
        // given
        Long id = 99L;
        BDDMockito.doThrow(new ProductNotFoundException(id)).when(productService).delete(id);

        // when and then
        mockMvc.perform(delete("/api/v1/products/" + id)
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$", is("Could not find product with id " + id)));
        verify(productService).delete(id);
    }
}
