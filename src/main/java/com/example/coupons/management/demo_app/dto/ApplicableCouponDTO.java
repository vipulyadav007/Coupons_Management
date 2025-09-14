package com.example.coupons.management.demo_app.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApplicableCouponDTO {
    private Long couponId;
    private String couponCode;
    private String couponType;
    private String description;
    private Double discountAmount;
}
