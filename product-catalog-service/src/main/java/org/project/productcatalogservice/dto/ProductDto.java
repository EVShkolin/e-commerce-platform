package org.project.productcatalogservice.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class ProductDto {

    private Long id;

    @NotBlank(message = "Name cannot not be blank")
    private String name;

    private String description;

    @Positive(message = "Price cannot be negative")
    private BigDecimal price;

    private String category;

    @Positive(message = "Rating cannot be negative")
    @Max(value = 5, message = "Rating cannot be above 5")
    private Float rating;

}
