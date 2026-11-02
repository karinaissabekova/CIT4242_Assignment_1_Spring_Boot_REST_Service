package com.karina.onlinestore.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;

public record UpdateProductRequest(
        @NotBlank @Size(min = 2, max = 100) String name,
        @Size(max = 1000) String description,
        @NotNull @Positive BigDecimal price,
        @NotBlank String category,
        Boolean inStock
) {}
