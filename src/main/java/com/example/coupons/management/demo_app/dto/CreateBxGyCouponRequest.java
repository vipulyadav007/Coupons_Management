package com.example.coupons.management.demo_app.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import java.time.LocalDate;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateBxGyCouponRequest {

    @NotBlank(message = "Coupon code is required")
    private String code;

    @NotNull(message = "Expiration date is required")
    private LocalDate expirationDate;

    private String description;

    @NotNull(message = "Buy products are required")
    private Map<Long, Integer> buyProducts; // productId -> required quantity

    @NotNull(message = "Get products are required")
    private Map<Long, Integer> getProducts; // productId -> free quantity

    @NotNull(message = "Repetition limit is required")
    @Positive(message = "Repetition limit must be positive")
    private Integer repetitionLimit;
}
