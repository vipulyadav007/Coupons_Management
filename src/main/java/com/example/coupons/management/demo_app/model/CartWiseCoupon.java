package com.example.coupons.management.demo_app.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDate;
import java.math.BigDecimal;

@Entity
@Table(name = "cart_wise_coupon", schema = "coupons")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartWiseCoupon implements CouponEntity {

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
    private CouponType couponType = CouponType.CART_WISE;

    @Column(name = "threshold", nullable = false, precision = 10, scale = 2)
    @NotNull(message = "Threshold is required")
    @DecimalMin(value = "0.01", message = "Threshold must be positive")
    private BigDecimal threshold;

    @Column(name = "discount_percentage", nullable = false, precision = 5, scale = 2)
    @NotNull(message = "Discount percentage is required")
    @DecimalMin(value = "0.01", message = "Discount percentage must be positive")
    @DecimalMax(value = "100.00", message = "Discount percentage cannot exceed 100")
    private BigDecimal discountPercentage;

    public double calculateDiscount(double cartValue) {
        if (cartValue >= threshold.doubleValue()) {
            return cartValue * (discountPercentage.doubleValue() / 100.0);
        }
        return 0.0;
    }

    @PrePersist
    public void prePersist() {
        if (couponType == null) {
            couponType = CouponType.CART_WISE;
        }
    }
}
