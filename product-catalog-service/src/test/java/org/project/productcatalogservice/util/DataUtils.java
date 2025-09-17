package org.project.productcatalogservice.util;

import org.project.productcatalogservice.dto.ProductDto;
import org.project.productcatalogservice.model.Product;

import java.math.BigDecimal;
import java.time.Instant;

public class DataUtils {

    public static Product getTestProduct1() {
        return new Product(1L,
                "Product 1",
                "Test product 1",
                BigDecimal.TEN,
                "Category1",
                4.5f,
                Instant.now(),
                Instant.now());
    }

    public static ProductDto getTestProductDto1() {
        return new ProductDto(1L,
                "Product 1",
                "Test product 1",
                BigDecimal.TEN,
                "Category1",
                4.5f);
    }

    public static Product getTestProduct2() {
        return new Product(2L,
                "Product 2",
                "Test product 2",
                BigDecimal.TEN,
                "Category2",
                4.5f,
                Instant.now(),
                Instant.now());
    }

    public static ProductDto getTestProductDto2() {
        return new ProductDto(2L,
                "Product 2",
                "Test product 2",
                BigDecimal.TEN,
                "Category2",
                4.5f);
    }

    public static Product getTestProduct3() {
        return new Product(3L,
                "Product 3",
                "Test product 3",
                BigDecimal.TEN,
                "Category3",
                4.5f,
                Instant.now(),
                Instant.now());
    }

    public static ProductDto getTestProductDto3() {
        return new ProductDto(3L,
                "Product 3",
                "Test product 3",
                BigDecimal.TEN,
                "Category3",
                4.5f);
    }

}
