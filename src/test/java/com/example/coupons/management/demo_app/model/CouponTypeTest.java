package com.example.coupons.management.demo_app.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CouponType Enum Tests")
class CouponTypeTest {

    @Test
    @DisplayName("Should have exactly three coupon types")
    void testCouponTypeValues() {
        // When
        CouponType[] types = CouponType.values();

        // Then
        assertEquals(3, types.length);
        assertArrayEquals(new CouponType[]{
            CouponType.CART_WISE,
            CouponType.PRODUCT_WISE,
            CouponType.BXGY
        }, types);
    }

    @Test
    @DisplayName("Should convert from string correctly")
    void testValueOf() {
        // When & Then
        assertEquals(CouponType.CART_WISE, CouponType.valueOf("CART_WISE"));
        assertEquals(CouponType.PRODUCT_WISE, CouponType.valueOf("PRODUCT_WISE"));
        assertEquals(CouponType.BXGY, CouponType.valueOf("BXGY"));
    }

    @Test
    @DisplayName("Should throw exception for invalid string")
    void testValueOfInvalidString() {
        // When & Then
        assertThrows(IllegalArgumentException.class, () -> {
            CouponType.valueOf("INVALID_TYPE");
        });
    }

    @Test
    @DisplayName("Should have correct string representation")
    void testToString() {
        // When & Then
        assertEquals("CART_WISE", CouponType.CART_WISE.toString());
        assertEquals("PRODUCT_WISE", CouponType.PRODUCT_WISE.toString());
        assertEquals("BXGY", CouponType.BXGY.toString());
    }

    @Test
    @DisplayName("Should support equality checks")
    void testEquality() {
        // When & Then
        assertEquals(CouponType.CART_WISE, CouponType.CART_WISE);
        assertNotEquals(CouponType.CART_WISE, CouponType.PRODUCT_WISE);
        assertNotEquals(CouponType.PRODUCT_WISE, CouponType.BXGY);
    }

    @Test
    @DisplayName("Should support ordinal values")
    void testOrdinal() {
        // When & Then
        assertEquals(0, CouponType.CART_WISE.ordinal());
        assertEquals(1, CouponType.PRODUCT_WISE.ordinal());
        assertEquals(2, CouponType.BXGY.ordinal());
    }
}
