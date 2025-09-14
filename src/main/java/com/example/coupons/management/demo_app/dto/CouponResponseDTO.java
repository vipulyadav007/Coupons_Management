package com.example.coupons.management.demo_app.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CouponResponseDTO {
    private Long id;
    private String code;
    private LocalDate expirationDate;
    private Boolean isActive;
    private String description;
    private String type; // "CART_WISE", "PRODUCT_WISE", "BXGY"

    // Cart-wise specific fields
    private Double threshold;
    private Double discountPercentage;

    // Product-wise specific fields
    private Long productId;

    // BxGy specific fields
    private Integer repetitionLimit;
}
