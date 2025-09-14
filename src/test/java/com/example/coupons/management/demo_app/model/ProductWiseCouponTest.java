package com.example.coupons.management.demo_app.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("ProductWiseCoupon Model Tests")
class ProductWiseCouponTest {

    private ProductWiseCoupon productWiseCoupon;

    @BeforeEach
    void setUp() {
        productWiseCoupon = new ProductWiseCoupon();
        productWiseCoupon.setCode("PROD15");
        productWiseCoupon.setDescription("15% off on Product 1001");
        productWiseCoupon.setExpirationDate(LocalDate.now().plusDays(30));
        productWiseCoupon.setIsActive(true);
        productWiseCoupon.setProductId(1001L);
        productWiseCoupon.setDiscountPercentage(new BigDecimal("15.00"));
    }

    @Nested
    @DisplayName("Constructor Tests")
    class ConstructorTests {

        @Test
        @DisplayName("Should create product-wise coupon with no-args constructor")
        void testNoArgsConstructor() {
            // When
            ProductWiseCoupon coupon = new ProductWiseCoupon();

            // Then
            assertNotNull(coupon);
            assertTrue(coupon.getIsActive()); // Default value
            assertEquals(CouponType.PRODUCT_WISE, coupon.getCouponType()); // Default value
        }

        @Test
        @DisplayName("Should create product-wise coupon with all-args constructor")
        void testAllArgsConstructor() {
            // Given
            Long id = 1L;
            String code = "PROD20";
            LocalDate expirationDate = LocalDate.now().plusDays(60);
            Boolean isActive = true;
            String description = "20% off product";
            CouponType couponType = CouponType.PRODUCT_WISE;
            Long productId = 2001L;
            BigDecimal discountPercentage = new BigDecimal("20.00");

            // When
            ProductWiseCoupon coupon = new ProductWiseCoupon(id, code, expirationDate, isActive,
                description, couponType, productId, discountPercentage);

            // Then
            assertEquals(id, coupon.getId());
            assertEquals(code, coupon.getCode());
            assertEquals(expirationDate, coupon.getExpirationDate());
            assertEquals(isActive, coupon.getIsActive());
            assertEquals(description, coupon.getDescription());
            assertEquals(couponType, coupon.getCouponType());
            assertEquals(productId, coupon.getProductId());
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
            String code = "TEST_PROD";
            LocalDate expirationDate = LocalDate.now().plusDays(15);
            Boolean isActive = false;
            String description = "Test description";
            Long productId = 5001L;
            BigDecimal discountPercentage = new BigDecimal("25.00");

            // When
            productWiseCoupon.setId(id);
            productWiseCoupon.setCode(code);
            productWiseCoupon.setExpirationDate(expirationDate);
            productWiseCoupon.setIsActive(isActive);
            productWiseCoupon.setDescription(description);
            productWiseCoupon.setProductId(productId);
            productWiseCoupon.setDiscountPercentage(discountPercentage);

            // Then
            assertEquals(id, productWiseCoupon.getId());
            assertEquals(code, productWiseCoupon.getCode());
            assertEquals(expirationDate, productWiseCoupon.getExpirationDate());
            assertEquals(isActive, productWiseCoupon.getIsActive());
            assertEquals(description, productWiseCoupon.getDescription());
            assertEquals(productId, productWiseCoupon.getProductId());
            assertEquals(discountPercentage, productWiseCoupon.getDiscountPercentage());
        }

        @Test
        @DisplayName("Should handle null values appropriately")
        void testNullValues() {
            // When
            productWiseCoupon.setDescription(null);

            // Then
            assertNull(productWiseCoupon.getDescription());
            assertNotNull(productWiseCoupon.getCode()); // Other fields should remain unchanged
        }
    }

    @Nested
    @DisplayName("Business Logic Tests")
    class BusinessLogicTests {

        @Test
        @DisplayName("Should calculate discount when product ID matches")
        void testCalculateDiscountWhenProductMatches() {
            // Given
            Long cartProductId = 1001L; // Matches coupon's product ID
            Integer quantity = 2;
            Double price = 50.0;

            // When
            double discount = productWiseCoupon.calculateDiscount(cartProductId, quantity, price);

            // Then
            assertEquals(15.0, discount, 0.01); // 15% of (2 * 50) = 15% of 100 = 15
        }

        @Test
        @DisplayName("Should return zero discount when product ID does not match")
        void testCalculateDiscountWhenProductDoesNotMatch() {
            // Given
            Long cartProductId = 2001L; // Does not match coupon's product ID
            Integer quantity = 2;
            Double price = 50.0;

            // When
            double discount = productWiseCoupon.calculateDiscount(cartProductId, quantity, price);

            // Then
            assertEquals(0.0, discount, 0.01);
        }

        @Test
        @DisplayName("Should calculate discount with single quantity")
        void testCalculateDiscountWithSingleQuantity() {
            // Given
            Long cartProductId = 1001L;
            Integer quantity = 1;
            Double price = 100.0;

            // When
            double discount = productWiseCoupon.calculateDiscount(cartProductId, quantity, price);

            // Then
            assertEquals(15.0, discount, 0.01); // 15% of 100
        }

        @Test
        @DisplayName("Should calculate discount with large quantity")
        void testCalculateDiscountWithLargeQuantity() {
            // Given
            Long cartProductId = 1001L;
            Integer quantity = 10;
            Double price = 20.0;

            // When
            double discount = productWiseCoupon.calculateDiscount(cartProductId, quantity, price);

            // Then
            assertEquals(30.0, discount, 0.01); // 15% of (10 * 20) = 15% of 200 = 30
        }

        @Test
        @DisplayName("Should handle zero price")
        void testCalculateDiscountWithZeroPrice() {
            // Given
            Long cartProductId = 1001L;
            Integer quantity = 5;
            Double price = 0.0;

            // When
            double discount = productWiseCoupon.calculateDiscount(cartProductId, quantity, price);

            // Then
            assertEquals(0.0, discount, 0.01);
        }

        @Test
        @DisplayName("Should handle zero quantity")
        void testCalculateDiscountWithZeroQuantity() {
            // Given
            Long cartProductId = 1001L;
            Integer quantity = 0;
            Double price = 50.0;

            // When
            double discount = productWiseCoupon.calculateDiscount(cartProductId, quantity, price);

            // Then
            assertEquals(0.0, discount, 0.01);
        }

        @Test
        @DisplayName("Should handle very small discount percentage")
        void testCalculateDiscountWithSmallPercentage() {
            // Given
            productWiseCoupon.setDiscountPercentage(new BigDecimal("0.01")); // 0.01%
            Long cartProductId = 1001L;
            Integer quantity = 1;
            Double price = 1000.0;

            // When
            double discount = productWiseCoupon.calculateDiscount(cartProductId, quantity, price);

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
            productWiseCoupon.setCouponType(null);

            // When
            productWiseCoupon.prePersist();

            // Then
            // Note: There's a bug in the original code - it sets CART_WISE instead of PRODUCT_WISE
            // This test documents the current behavior
            assertEquals(CouponType.CART_WISE, productWiseCoupon.getCouponType());
        }

        @Test
        @DisplayName("Should not change existing coupon type")
        void testPrePersistDoesNotChangeExistingCouponType() {
            // Given
            productWiseCoupon.setCouponType(CouponType.PRODUCT_WISE);

            // When
            productWiseCoupon.prePersist();

            // Then
            assertEquals(CouponType.PRODUCT_WISE, productWiseCoupon.getCouponType());
        }
    }

    @Nested
    @DisplayName("CouponEntity Interface Implementation Tests")
    class CouponEntityImplementationTests {

        @Test
        @DisplayName("Should implement CouponEntity interface correctly")
        void testCouponEntityImplementation() {
            // Given
            CouponEntity couponEntity = productWiseCoupon;

            // When & Then
            assertEquals(productWiseCoupon.getCode(), couponEntity.getCode());
            assertEquals(productWiseCoupon.getExpirationDate(), couponEntity.getExpirationDate());
            assertEquals(productWiseCoupon.getIsActive(), couponEntity.getIsActive());
            assertEquals(productWiseCoupon.getDescription(), couponEntity.getDescription());

            // Test setters through interface
            couponEntity.setCode("NEW_PROD_CODE");
            assertEquals("NEW_PROD_CODE", productWiseCoupon.getCode());
        }
    }

    @Nested
    @DisplayName("Equals and HashCode Tests")
    class EqualsHashCodeTests {

        @Test
        @DisplayName("Should be equal when all properties are same")
        void testEqualsWhenSame() {
            // Given
            ProductWiseCoupon coupon1 = new ProductWiseCoupon();
            coupon1.setCode("SAME_CODE");
            coupon1.setProductId(1001L);
            coupon1.setDiscountPercentage(new BigDecimal("15.00"));

            ProductWiseCoupon coupon2 = new ProductWiseCoupon();
            coupon2.setCode("SAME_CODE");
            coupon2.setProductId(1001L);
            coupon2.setDiscountPercentage(new BigDecimal("15.00"));

            // When & Then
            assertEquals(coupon1, coupon2);
            assertEquals(coupon1.hashCode(), coupon2.hashCode());
        }

        @Test
        @DisplayName("Should not be equal when properties differ")
        void testNotEqualsWhenDifferent() {
            // Given
            ProductWiseCoupon coupon1 = new ProductWiseCoupon();
            coupon1.setCode("CODE1");
            coupon1.setProductId(1001L);

            ProductWiseCoupon coupon2 = new ProductWiseCoupon();
            coupon2.setCode("CODE2");
            coupon2.setProductId(1002L);

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
            String toString = productWiseCoupon.toString();

            // Then
            assertNotNull(toString);
            assertTrue(toString.contains("PROD15"));
            assertTrue(toString.contains("ProductWiseCoupon"));
        }
    }


        @Test
        @DisplayName("Should handle null cart product ID in calculation")
        void testCalculateDiscountWithNullCartProductId() {
            // Given
            Long cartProductId = null;
            Integer quantity = 1;
            Double price = 50.0;

            // When & Then
            assertThrows(NullPointerException.class, () -> {
                productWiseCoupon.calculateDiscount(cartProductId, quantity, price);
            });
        }
}
