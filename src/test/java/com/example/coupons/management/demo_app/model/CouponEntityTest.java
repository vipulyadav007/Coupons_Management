package com.example.coupons.management.demo_app.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.BeforeEach;

import java.time.LocalDate;
import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CouponEntity Interface Tests")
class CouponEntityTest {

    private CouponEntity cartWiseCouponEntity;
    private CouponEntity productWiseCouponEntity;
    private CouponEntity bxGyCouponEntity;

    @BeforeEach
    void setUp() {
        // Create instances of each implementation
        CartWiseCoupon cartWiseCoupon = new CartWiseCoupon();
        cartWiseCoupon.setCode("CART_TEST");
        cartWiseCoupon.setDescription("Cart test coupon");
        cartWiseCoupon.setExpirationDate(LocalDate.now().plusDays(30));
        cartWiseCoupon.setIsActive(true);
        cartWiseCoupon.setThreshold(new BigDecimal("100.00"));
        cartWiseCoupon.setDiscountPercentage(new BigDecimal("10.00"));
        cartWiseCouponEntity = cartWiseCoupon;

        ProductWiseCoupon productWiseCoupon = new ProductWiseCoupon();
        productWiseCoupon.setCode("PROD_TEST");
        productWiseCoupon.setDescription("Product test coupon");
        productWiseCoupon.setExpirationDate(LocalDate.now().plusDays(30));
        productWiseCoupon.setIsActive(true);
        productWiseCoupon.setProductId(1001L);
        productWiseCoupon.setDiscountPercentage(new BigDecimal("15.00"));
        productWiseCouponEntity = productWiseCoupon;

        BxGyCoupon bxGyCoupon = new BxGyCoupon();
        bxGyCoupon.setCode("BXGY_TEST");
        bxGyCoupon.setDescription("BxGy test coupon");
        bxGyCoupon.setExpirationDate(LocalDate.now().plusDays(30));
        bxGyCoupon.setIsActive(true);
        bxGyCoupon.setRepetitionLimit(2);
        bxGyCouponEntity = bxGyCoupon;
    }

    @Test
    @DisplayName("Should get and set code for all implementations")
    void testCodeGetterSetter() {
        // Test CartWiseCoupon
        assertEquals("CART_TEST", cartWiseCouponEntity.getCode());
        cartWiseCouponEntity.setCode("NEW_CART_CODE");
        assertEquals("NEW_CART_CODE", cartWiseCouponEntity.getCode());

        // Test ProductWiseCoupon
        assertEquals("PROD_TEST", productWiseCouponEntity.getCode());
        productWiseCouponEntity.setCode("NEW_PROD_CODE");
        assertEquals("NEW_PROD_CODE", productWiseCouponEntity.getCode());

        // Test BxGyCoupon
        assertEquals("BXGY_TEST", bxGyCouponEntity.getCode());
        bxGyCouponEntity.setCode("NEW_BXGY_CODE");
        assertEquals("NEW_BXGY_CODE", bxGyCouponEntity.getCode());
    }

    @Test
    @DisplayName("Should get and set expiration date for all implementations")
    void testExpirationDateGetterSetter() {
        // Given
        LocalDate newDate = LocalDate.now().plusDays(60);

        // Test CartWiseCoupon
        assertNotNull(cartWiseCouponEntity.getExpirationDate());
        cartWiseCouponEntity.setExpirationDate(newDate);
        assertEquals(newDate, cartWiseCouponEntity.getExpirationDate());

        // Test ProductWiseCoupon
        assertNotNull(productWiseCouponEntity.getExpirationDate());
        productWiseCouponEntity.setExpirationDate(newDate);
        assertEquals(newDate, productWiseCouponEntity.getExpirationDate());

        // Test BxGyCoupon
        assertNotNull(bxGyCouponEntity.getExpirationDate());
        bxGyCouponEntity.setExpirationDate(newDate);
        assertEquals(newDate, bxGyCouponEntity.getExpirationDate());
    }

    @Test
    @DisplayName("Should get and set active status for all implementations")
    void testIsActiveGetterSetter() {
        // Test CartWiseCoupon
        assertTrue(cartWiseCouponEntity.getIsActive());
        cartWiseCouponEntity.setIsActive(false);
        assertFalse(cartWiseCouponEntity.getIsActive());

        // Test ProductWiseCoupon
        assertTrue(productWiseCouponEntity.getIsActive());
        productWiseCouponEntity.setIsActive(false);
        assertFalse(productWiseCouponEntity.getIsActive());

        // Test BxGyCoupon
        assertTrue(bxGyCouponEntity.getIsActive());
        bxGyCouponEntity.setIsActive(false);
        assertFalse(bxGyCouponEntity.getIsActive());
    }

    @Test
    @DisplayName("Should get and set description for all implementations")
    void testDescriptionGetterSetter() {
        // Given
        String newDescription = "Updated description";

        // Test CartWiseCoupon
        assertEquals("Cart test coupon", cartWiseCouponEntity.getDescription());
        cartWiseCouponEntity.setDescription(newDescription);
        assertEquals(newDescription, cartWiseCouponEntity.getDescription());

        // Test ProductWiseCoupon
        assertEquals("Product test coupon", productWiseCouponEntity.getDescription());
        productWiseCouponEntity.setDescription(newDescription);
        assertEquals(newDescription, productWiseCouponEntity.getDescription());

        // Test BxGyCoupon
        assertEquals("BxGy test coupon", bxGyCouponEntity.getDescription());
        bxGyCouponEntity.setDescription(newDescription);
        assertEquals(newDescription, bxGyCouponEntity.getDescription());
    }

    @Test
    @DisplayName("Should get ID for all implementations")
    void testGetId() {
        // Test that getId() method is available and returns null for new entities
        assertNull(cartWiseCouponEntity.getId());
        assertNull(productWiseCouponEntity.getId());
        assertNull(bxGyCouponEntity.getId());
    }

    @Test
    @DisplayName("Should handle null values appropriately")
    void testNullValues() {
        // Test setting null values
        cartWiseCouponEntity.setDescription(null);
        assertNull(cartWiseCouponEntity.getDescription());

        productWiseCouponEntity.setDescription(null);
        assertNull(productWiseCouponEntity.getDescription());

        bxGyCouponEntity.setDescription(null);
        assertNull(bxGyCouponEntity.getDescription());
    }

    @Test
    @DisplayName("Should work polymorphically")
    void testPolymorphism() {
        // Given
        CouponEntity[] entities = {
            cartWiseCouponEntity,
            productWiseCouponEntity,
            bxGyCouponEntity
        };

        // When & Then
        for (CouponEntity entity : entities) {
            assertNotNull(entity.getCode());
            assertNotNull(entity.getExpirationDate());
            assertNotNull(entity.getIsActive());
            assertNotNull(entity.getDescription());

            // Test that we can call methods polymorphically
            String originalCode = entity.getCode();
            entity.setCode("POLY_TEST");
            assertEquals("POLY_TEST", entity.getCode());
            entity.setCode(originalCode); // Restore
        }
    }

    @Test
    @DisplayName("Should maintain type information when accessed through interface")
    void testTypeInformation() {
        // When & Then
        assertTrue(cartWiseCouponEntity instanceof CartWiseCoupon);
        assertTrue(productWiseCouponEntity instanceof ProductWiseCoupon);
        assertTrue(bxGyCouponEntity instanceof BxGyCoupon);

        // All should also be instances of CouponEntity
        assertTrue(cartWiseCouponEntity instanceof CouponEntity);
        assertTrue(productWiseCouponEntity instanceof CouponEntity);
        assertTrue(bxGyCouponEntity instanceof CouponEntity);
    }

    @Test
    @DisplayName("Should allow casting back to concrete types")
    void testCastingToConcreteTypes() {
        // When & Then
        CartWiseCoupon cartCoupon = (CartWiseCoupon) cartWiseCouponEntity;
        assertNotNull(cartCoupon.getThreshold());
        assertNotNull(cartCoupon.getDiscountPercentage());

        ProductWiseCoupon productCoupon = (ProductWiseCoupon) productWiseCouponEntity;
        assertNotNull(productCoupon.getProductId());
        assertNotNull(productCoupon.getDiscountPercentage());

        BxGyCoupon bxGyCoupon = (BxGyCoupon) bxGyCouponEntity;
        assertNotNull(bxGyCoupon.getRepetitionLimit());
    }
}
