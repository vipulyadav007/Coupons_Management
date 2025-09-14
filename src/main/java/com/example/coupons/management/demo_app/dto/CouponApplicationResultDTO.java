package com.example.coupons.management.demo_app.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CouponApplicationResultDTO {
    private List<ItemDTO> updatedItems;
    private Double originalTotal;
    private Double discountAmount;
    private Double finalTotal;
    private String appliedCouponCode;
    private String message;
}
