package com.example.coupons.management.demo_app.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDate;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;

@Entity
@Table(name = "bxgy_coupon", schema = "coupons")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BxGyCoupon implements CouponEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "hibernate_sequence")
    @SequenceGenerator(name = "hibernate_sequence", sequenceName = "coupons.hibernate_sequence", allocationSize = 1)
    private Long id;

    @Column(name = "code", unique = true, nullable = false)
    @NotBlank(message = "Coupon code cannot be blank")
    @Size(max = 255, message = "Coupon code cannot exceed 255 characters")
    private String code;

    @Column(name = "expiration_date", nullable = false)
    @NotNull(message = "Expiration date is required")
    @Future(message = "Expiration date must be in the future")
    private LocalDate expirationDate;

    @Column(name = "is_active", nullable = false)
    @NotNull(message = "Active status is required")
    private Boolean isActive = true;

    @Column(name = "description")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "coupon_type", nullable = false, length = 50)
    private CouponType couponType = CouponType.BXGY;

    @ElementCollection
    @CollectionTable(
        name = "bxgy_buy_products",
        schema = "coupons",
        joinColumns = @JoinColumn(name = "coupon_id")
    )
    @MapKeyColumn(name = "product_id")
    @Column(name = "quantity")
    @NotEmpty(message = "Buy products cannot be empty")
    private Map<@NotNull @Min(1) Long, @NotNull @Min(1) Integer> buyProducts;

    @ElementCollection
    @CollectionTable(
        name = "bxgy_get_products",
        schema = "coupons",
        joinColumns = @JoinColumn(name = "coupon_id")
    )
    @MapKeyColumn(name = "product_id")
    @Column(name = "quantity")
    @NotEmpty(message = "Get products cannot be empty")
    private Map<@NotNull @Min(1) Long, @NotNull @Min(1) Integer> getProducts;

    @Column(name = "repetition_limit", nullable = false)
    @NotNull(message = "Repetition limit is required")
    @Min(value = 1, message = "Repetition limit must be positive")
    private Integer repetitionLimit;

    public double calculateDiscount(Map<Long, Integer> cartItems, Map<Long, Double> productPrices) {
        if (!isApplicable(cartItems)) {
            return 0.0;
        }

        int applicableTimes = calculateApplicableTimes(cartItems);

        // For BxGy coupons, calculate discount based on the cheapest buy products
        // since we don't know the prices of free products
        double totalDiscount = 0.0;

        // Option 1: Calculate discount based on cheapest buy products
        List<Double> buyProductPrices = new ArrayList<>();
        for (Map.Entry<Long, Integer> buyProduct : buyProducts.entrySet()) {
            Long productId = buyProduct.getKey();
            Double price = productPrices.getOrDefault(productId, 0.0);
            if (price > 0.0) {
                buyProductPrices.add(price);
            }
        }

        if (!buyProductPrices.isEmpty()) {
            // Use the cheapest buy product price as estimate for free products
            double cheapestPrice = buyProductPrices.stream().min(Double::compareTo).orElse(0.0);

            // Calculate total free quantity
            int totalFreeQuantity = getProducts.values().stream().mapToInt(Integer::intValue).sum();

            // Discount = cheapest price * total free quantity * applicable times
            totalDiscount = cheapestPrice * totalFreeQuantity * applicableTimes;
        }

        return totalDiscount;
    }

    /**
     * Check if this BxGy coupon is applicable to the given cart items
     */
    public boolean isApplicable(Map<Long, Integer> cartItems) {
        if (cartItems == null || buyProducts == null || getProducts == null) {
            return false;
        }

        // Check if all buy products are available in required quantities
        for (Map.Entry<Long, Integer> buyProduct : buyProducts.entrySet()) {
            Long productId = buyProduct.getKey();
            Integer requiredQty = buyProduct.getValue();
            Integer availableQty = cartItems.getOrDefault(productId, 0);

            if (availableQty < requiredQty) {
                return false; // Not applicable
            }
        }

        return true;
    }

    /**
     * Calculate how many times this offer can be applied
     */
    public int calculateApplicableTimes(Map<Long, Integer> cartItems) {
        if (!isApplicable(cartItems)) {
            return 0;
        }

        int applicableTimes = Integer.MAX_VALUE;

        // Check how many times we can apply this offer based on buy products
        for (Map.Entry<Long, Integer> buyProduct : buyProducts.entrySet()) {
            Long productId = buyProduct.getKey();
            Integer requiredQty = buyProduct.getValue();
            Integer availableQty = cartItems.getOrDefault(productId, 0);

            applicableTimes = Math.min(applicableTimes, availableQty / requiredQty);
        }

        // Apply repetition limit
        applicableTimes = Math.min(applicableTimes, repetitionLimit);

        return applicableTimes;
    }

    @PrePersist
    public void prePersist() {
        if (couponType == null) {
            couponType = CouponType.BXGY;
        }
    }
}
