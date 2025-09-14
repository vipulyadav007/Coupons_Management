package com.example.coupons.management.demo_app.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CartWiseCoupon Model Tests")
class CartWiseCouponTest {

    private CartWiseCoupon cartWiseCoupon;

    @BeforeEach
    void setUp() {
        cartWiseCoupon = new CartWiseCoupon();
        cartWiseCoupon.setCode("CART10");
        cartWiseCoupon.setDescription("10% off on orders above $100");
        cartWiseCoupon.setExpirationDate(LocalDate.now().plusDays(30));
        cartWiseCoupon.setIsActive(true);
        cartWiseCoupon.setThreshold(new BigDecimal("100.00"));
        cartWiseCoupon.setDiscountPercentage(new BigDecimal("10.00"));
    }

    @Nested
    @DisplayName("Constructor Tests")
    class ConstructorTests {

        @Test
        @DisplayName("Should create cart-wise coupon with no-args constructor")
        void testNoArgsConstructor() {
            // When
            CartWiseCoupon coupon = new CartWiseCoupon();

            // Then
            assertNotNull(coupon);
            assertTrue(coupon.getIsActive()); // Default value
            assertEquals(CouponType.CART_WISE, coupon.getCouponType()); // Default value
        }

        @Test
        @DisplayName("Should create cart-wise coupon with all-args constructor")
        void testAllArgsConstructor() {
            // Given
            Long id = 1L;
            String code = "CART20";
            LocalDate expirationDate = LocalDate.now().plusDays(60);
            Boolean isActive = true;
            String description = "20% off cart";
            CouponType couponType = CouponType.CART_WISE;
            BigDecimal threshold = new BigDecimal("200.00");
            BigDecimal discountPercentage = new BigDecimal("20.00");

            // When
            CartWiseCoupon coupon = new CartWiseCoupon(id, code, expirationDate, isActive,
                description, couponType, threshold, discountPercentage);

            // Then
            assertEquals(id, coupon.getId());
            assertEquals(code, coupon.getCode());
            assertEquals(expirationDate, coupon.getExpirationDate());
            assertEquals(isActive, coupon.getIsActive());
            assertEquals(description, coupon.getDescription());
            assertEquals(couponType, coupon.getCouponType());
            assertEquals(threshold, coupon.getThreshold());
            assertEquals(discountPercentage, coupon.getDiscountPercentage());
        }
    }

    @Nested
    @DisplayName("Getter and Setter Tests")
    class GetterSetterTests {

        @Test
        @DisplayName("Should set and get all properties correctly")
        void testGettersAndSetters() {
            // Given
            Long id = 1L;
            String code = "TEST_CART";
            LocalDate expirationDate = LocalDate.now().plusDays(15);
            Boolean isActive = false;
            String description = "Test description";
            BigDecimal threshold = new BigDecimal("150.00");
            BigDecimal discountPercentage = new BigDecimal("15.00");

            // When
            cartWiseCoupon.setId(id);
            cartWiseCoupon.setCode(code);
            cartWiseCoupon.setExpirationDate(expirationDate);
            cartWiseCoupon.setIsActive(isActive);
            cartWiseCoupon.setDescription(description);
            cartWiseCoupon.setThreshold(threshold);
            cartWiseCoupon.setDiscountPercentage(discountPercentage);

            // Then
            assertEquals(id, cartWiseCoupon.getId());
            assertEquals(code, cartWiseCoupon.getCode());
            assertEquals(expirationDate, cartWiseCoupon.getExpirationDate());
            assertEquals(isActive, cartWiseCoupon.getIsActive());
            assertEquals(description, cartWiseCoupon.getDescription());
            assertEquals(threshold, cartWiseCoupon.getThreshold());
            assertEquals(discountPercentage, cartWiseCoupon.getDiscountPercentage());
        }

        @Test
        @DisplayName("Should handle null values appropriately")
        void testNullValues() {
            // When
            cartWiseCoupon.setDescription(null);

            // Then
            assertNull(cartWiseCoupon.getDescription());
            assertNotNull(cartWiseCoupon.getCode()); // Other fields should remain unchanged
        }
    }

    @Nested
    @DisplayName("Business Logic Tests")
    class BusinessLogicTests {

        @Test
        @DisplayName("Should calculate discount when cart value meets threshold")
        void testCalculateDiscountWhenThresholdMet() {
            // Given
            double cartValue = 150.0; // Above threshold of 100

            // When
            double discount = cartWiseCoupon.calculateDiscount(cartValue);

            // Then
            assertEquals(15.0, discount, 0.01); // 10% of 150
        }

        @Test
        @DisplayName("Should return zero discount when cart value below threshold")
        void testCalculateDiscountWhenThresholdNotMet() {
            // Given
            double cartValue = 50.0; // Below threshold of 100

            // When
            double discount = cartWiseCoupon.calculateDiscount(cartValue);

            // Then
            assertEquals(0.0, discount, 0.01);
        }

        @Test
        @DisplayName("Should calculate discount when cart value equals threshold")
        void testCalculateDiscountWhenCartValueEqualsThreshold() {
            // Given
            double cartValue = 100.0; // Equals threshold

            // When
            double discount = cartWiseCoupon.calculateDiscount(cartValue);

            // Then
            assertEquals(10.0, discount, 0.01); // 10% of 100
        }

        @Test
        @DisplayName("Should handle large cart values correctly")
        void testCalculateDiscountWithLargeCartValue() {
            // Given
            double cartValue = 1000.0;

            // When
            double discount = cartWiseCoupon.calculateDiscount(cartValue);

            // Then
            assertEquals(100.0, discount, 0.01); // 10% of 1000
        }

        @Test
        @DisplayName("Should handle edge case with very small discount percentage")
        void testCalculateDiscountWithSmallPercentage() {
            // Given
            cartWiseCoupon.setDiscountPercentage(new BigDecimal("0.01")); // 0.01%
            double cartValue = 1000.0;

            // When
            double discount = cartWiseCoupon.calculateDiscount(cartValue);

            // Then
            assertEquals(0.1, discount, 0.001); // 0.01% of 1000
        }
    }

    @Nested
    @DisplayName("PrePersist Tests")
    class PrePersistTests {

        @Test
        @DisplayName("Should set default coupon type when null")
        void testPrePersistSetsDefaultCouponType() {
            // Given
            cartWiseCoupon.setCouponType(null);

            // When
            cartWiseCoupon.prePersist();

            // Then
            assertEquals(CouponType.CART_WISE, cartWiseCoupon.getCouponType());
        }

        @Test
        @DisplayName("Should not change existing coupon type")
        void testPrePersistDoesNotChangeExistingCouponType() {
            // Given
            cartWiseCoupon.setCouponType(CouponType.CART_WISE);

            // When
            cartWiseCoupon.prePersist();

            // Then
            assertEquals(CouponType.CART_WISE, cartWiseCoupon.getCouponType());
        }
    }

    @Nested
    @DisplayName("CouponEntity Interface Implementation Tests")
    class CouponEntityImplementationTests {

        @Test
        @DisplayName("Should implement CouponEntity interface correctly")
        void testCouponEntityImplementation() {
            // Given
            CouponEntity couponEntity = cartWiseCoupon;

            // When & Then
            assertEquals(cartWiseCoupon.getCode(), couponEntity.getCode());
            assertEquals(cartWiseCoupon.getExpirationDate(), couponEntity.getExpirationDate());
            assertEquals(cartWiseCoupon.getIsActive(), couponEntity.getIsActive());
            assertEquals(cartWiseCoupon.getDescription(), couponEntity.getDescription());

            // Test setters through interface
            couponEntity.setCode("NEW_CODE");
            assertEquals("NEW_CODE", cartWiseCoupon.getCode());
        }
    }

    @Nested
    @DisplayName("Equals and HashCode Tests")
    class EqualsHashCodeTests {

        @Test
        @DisplayName("Should be equal when all properties are same")
        void testEqualsWhenSame() {
            // Given
            CartWiseCoupon coupon1 = new CartWiseCoupon();
            coupon1.setCode("SAME_CODE");
            coupon1.setThreshold(new BigDecimal("100.00"));
            coupon1.setDiscountPercentage(new BigDecimal("10.00"));

            CartWiseCoupon coupon2 = new CartWiseCoupon();
            coupon2.setCode("SAME_CODE");
            coupon2.setThreshold(new BigDecimal("100.00"));
            coupon2.setDiscountPercentage(new BigDecimal("10.00"));

            // When & Then
            assertEquals(coupon1, coupon2);
            assertEquals(coupon1.hashCode(), coupon2.hashCode());
        }

        @Test
        @DisplayName("Should not be equal when properties differ")
        void testNotEqualsWhenDifferent() {
            // Given
            CartWiseCoupon coupon1 = new CartWiseCoupon();
            coupon1.setCode("CODE1");

            CartWiseCoupon coupon2 = new CartWiseCoupon();
            coupon2.setCode("CODE2");

            // When & Then
            assertNotEquals(coupon1, coupon2);
        }
    }

    @Nested
    @DisplayName("ToString Tests")
    class ToStringTests {

        @Test
        @DisplayName("Should generate proper string representation")
        void testToString() {
            // When
            String toString = cartWiseCoupon.toString();

            // Then
            assertNotNull(toString);
            assertTrue(toString.contains("CART10"));
            assertTrue(toString.contains("CartWiseCoupon"));
        }
    }
}
