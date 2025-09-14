package com.example.coupons.management.demo_app.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("BxGyCoupon Model Tests")
class BxGyCouponTest {

    private BxGyCoupon bxGyCoupon;
    private Map<Long, Integer> buyProducts;
    private Map<Long, Integer> getProducts;
    private Map<Long, Integer> cartItems;
    private Map<Long, Double> productPrices;

    @BeforeEach
    void setUp() {
        bxGyCoupon = new BxGyCoupon();
        bxGyCoupon.setCode("BUY2GET1");
        bxGyCoupon.setDescription("Buy 2 get 1 free on selected items");
        bxGyCoupon.setExpirationDate(LocalDate.now().plusDays(30));
        bxGyCoupon.setIsActive(true);
        bxGyCoupon.setRepetitionLimit(2);

        // Setup buy products: Need 2 of product 201 and 1 of product 202
        buyProducts = new HashMap<>();
        buyProducts.put(201L, 2);
        buyProducts.put(202L, 1);
        bxGyCoupon.setBuyProducts(buyProducts);

        // Setup get products: Get 1 of product 301
        getProducts = new HashMap<>();
        getProducts.put(301L, 1);
        bxGyCoupon.setGetProducts(getProducts);

        // Setup test cart items
        cartItems = new HashMap<>();
        cartItems.put(201L, 4); // More than required
        cartItems.put(202L, 2); // More than required
        cartItems.put(301L, 1); // Available for free

        // Setup product prices
        productPrices = new HashMap<>();
        productPrices.put(201L, 25.0); // Cheapest buy product
        productPrices.put(202L, 50.0);
        productPrices.put(301L, 30.0);
    }

    @Nested
    @DisplayName("Constructor Tests")
    class ConstructorTests {

        @Test
        @DisplayName("Should create BxGy coupon with no-args constructor")
        void testNoArgsConstructor() {
            // When
            BxGyCoupon coupon = new BxGyCoupon();

            // Then
            assertNotNull(coupon);
            assertTrue(coupon.getIsActive()); // Default value
            assertEquals(CouponType.BXGY, coupon.getCouponType()); // Default value
        }

        @Test
        @DisplayName("Should create BxGy coupon with all-args constructor")
        void testAllArgsConstructor() {
            // Given
            Long id = 1L;
            String code = "COMPLEX_BXGY";
            LocalDate expirationDate = LocalDate.now().plusDays(60);
            Boolean isActive = true;
            String description = "Complex BxGy offer";
            CouponType couponType = CouponType.BXGY;
            Map<Long, Integer> buyProds = Map.of(401L, 1);
            Map<Long, Integer> getProds = Map.of(501L, 1);
            Integer repetitionLimit = 3;

            // When
            BxGyCoupon coupon = new BxGyCoupon(id, code, expirationDate, isActive,
                description, couponType, buyProds, getProds, repetitionLimit);

            // Then
            assertEquals(id, coupon.getId());
            assertEquals(code, coupon.getCode());
            assertEquals(expirationDate, coupon.getExpirationDate());
            assertEquals(isActive, coupon.getIsActive());
            assertEquals(description, coupon.getDescription());
            assertEquals(couponType, coupon.getCouponType());
            assertEquals(buyProds, coupon.getBuyProducts());
            assertEquals(getProds, coupon.getGetProducts());
            assertEquals(repetitionLimit, coupon.getRepetitionLimit());
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
            String code = "TEST_BXGY";
            LocalDate expirationDate = LocalDate.now().plusDays(15);
            Boolean isActive = false;
            String description = "Test description";
            Integer repetitionLimit = 5;

            Map<Long, Integer> newBuyProducts = Map.of(701L, 3);
            Map<Long, Integer> newGetProducts = Map.of(801L, 2);

            // When
            bxGyCoupon.setId(id);
            bxGyCoupon.setCode(code);
            bxGyCoupon.setExpirationDate(expirationDate);
            bxGyCoupon.setIsActive(isActive);
            bxGyCoupon.setDescription(description);
            bxGyCoupon.setRepetitionLimit(repetitionLimit);
            bxGyCoupon.setBuyProducts(newBuyProducts);
            bxGyCoupon.setGetProducts(newGetProducts);

            // Then
            assertEquals(id, bxGyCoupon.getId());
            assertEquals(code, bxGyCoupon.getCode());
            assertEquals(expirationDate, bxGyCoupon.getExpirationDate());
            assertEquals(isActive, bxGyCoupon.getIsActive());
            assertEquals(description, bxGyCoupon.getDescription());
            assertEquals(repetitionLimit, bxGyCoupon.getRepetitionLimit());
            assertEquals(newBuyProducts, bxGyCoupon.getBuyProducts());
            assertEquals(newGetProducts, bxGyCoupon.getGetProducts());
        }
    }

    @Nested
    @DisplayName("IsApplicable Tests")
    class IsApplicableTests {

        @Test
        @DisplayName("Should be applicable when cart has sufficient buy products")
        void testIsApplicableWhenSufficientProducts() {
            // When
            boolean result = bxGyCoupon.isApplicable(cartItems);

            // Then
            assertTrue(result);
        }

        @Test
        @DisplayName("Should not be applicable when cart lacks required buy products")
        void testIsNotApplicableWhenInsufficientProducts() {
            // Given
            Map<Long, Integer> insufficientCart = new HashMap<>();
            insufficientCart.put(201L, 1); // Need 2, have 1
            insufficientCart.put(202L, 1); // Need 1, have 1

            // When
            boolean result = bxGyCoupon.isApplicable(insufficientCart);

            // Then
            assertFalse(result);
        }

        @Test
        @DisplayName("Should not be applicable when cart is missing one required product")
        void testIsNotApplicableWhenMissingProduct() {
            // Given
            Map<Long, Integer> incompleteCart = new HashMap<>();
            incompleteCart.put(201L, 5); // Have enough of this
            // Missing product 202

            // When
            boolean result = bxGyCoupon.isApplicable(incompleteCart);

            // Then
            assertFalse(result);
        }

        @Test
        @DisplayName("Should not be applicable when cart is null")
        void testIsNotApplicableWhenCartIsNull() {
            // When
            boolean result = bxGyCoupon.isApplicable(null);

            // Then
            assertFalse(result);
        }

        @Test
        @DisplayName("Should not be applicable when buy products is null")
        void testIsNotApplicableWhenBuyProductsIsNull() {
            // Given
            bxGyCoupon.setBuyProducts(null);

            // When
            boolean result = bxGyCoupon.isApplicable(cartItems);

            // Then
            assertFalse(result);
        }

        @Test
        @DisplayName("Should not be applicable when get products is null")
        void testIsNotApplicableWhenGetProductsIsNull() {
            // Given
            bxGyCoupon.setGetProducts(null);

            // When
            boolean result = bxGyCoupon.isApplicable(cartItems);

            // Then
            assertFalse(result);
        }

        @Test
        @DisplayName("Should be applicable when cart has exactly required quantities")
        void testIsApplicableWithExactQuantities() {
            // Given
            Map<Long, Integer> exactCart = new HashMap<>();
            exactCart.put(201L, 2); // Exactly what's needed
            exactCart.put(202L, 1); // Exactly what's needed

            // When
            boolean result = bxGyCoupon.isApplicable(exactCart);

            // Then
            assertTrue(result);
        }
    }

    @Nested
    @DisplayName("CalculateApplicableTimes Tests")
    class CalculateApplicableTimesTests {

        @Test
        @DisplayName("Should calculate applicable times correctly")
        void testCalculateApplicableTimesNormal() {
            // Given: Cart has 4 of product 201 (need 2) and 2 of product 202 (need 1)
            // So can apply 2 times based on product 201, and 2 times based on product 202
            // Minimum is 2, but repetition limit is also 2

            // When
            int times = bxGyCoupon.calculateApplicableTimes(cartItems);

            // Then
            assertEquals(2, times); // Limited by repetition limit
        }

        @Test
        @DisplayName("Should return 0 when not applicable")
        void testCalculateApplicableTimesWhenNotApplicable() {
            // Given
            Map<Long, Integer> insufficientCart = new HashMap<>();
            insufficientCart.put(201L, 1); // Need 2, have 1

            // When
            int times = bxGyCoupon.calculateApplicableTimes(insufficientCart);

            // Then
            assertEquals(0, times);
        }

        @Test
        @DisplayName("Should be limited by repetition limit")
        void testCalculateApplicableTimesLimitedByRepetitionLimit() {
            // Given
            bxGyCoupon.setRepetitionLimit(1); // Set very low limit
            Map<Long, Integer> abundantCart = new HashMap<>();
            abundantCart.put(201L, 10); // Way more than needed
            abundantCart.put(202L, 10); // Way more than needed

            // When
            int times = bxGyCoupon.calculateApplicableTimes(abundantCart);

            // Then
            assertEquals(1, times); // Limited by repetition limit
        }

        @Test
        @DisplayName("Should be limited by product availability")
        void testCalculateApplicableTimesLimitedByProductAvailability() {
            // Given
            bxGyCoupon.setRepetitionLimit(10); // High limit
            Map<Long, Integer> limitedCart = new HashMap<>();
            limitedCart.put(201L, 3); // Can apply 1 time (need 2, have 3)
            limitedCart.put(202L, 5); // Can apply 5 times (need 1, have 5)

            // When
            int times = bxGyCoupon.calculateApplicableTimes(limitedCart);

            // Then
            assertEquals(1, times); // Limited by product 201 availability
        }
    }

    @Nested
    @DisplayName("CalculateDiscount Tests")
    class CalculateDiscountTests {

        @Test
        @DisplayName("Should calculate discount correctly when applicable")
        void testCalculateDiscountWhenApplicable() {
            // Given: Can apply 2 times, cheapest buy product is 25.0, get 1 product free each time
            // Expected: 2 applications * 1 free product * 25.0 (cheapest price) = 50.0

            // When
            double discount = bxGyCoupon.calculateDiscount(cartItems, productPrices);

            // Then
            assertEquals(50.0, discount, 0.01);
        }

        @Test
        @DisplayName("Should return 0 discount when not applicable")
        void testCalculateDiscountWhenNotApplicable() {
            // Given
            Map<Long, Integer> insufficientCart = new HashMap<>();
            insufficientCart.put(201L, 1); // Not enough

            // When
            double discount = bxGyCoupon.calculateDiscount(insufficientCart, productPrices);

            // Then
            assertEquals(0.0, discount, 0.01);
        }

        @Test
        @DisplayName("Should handle multiple get products")
        void testCalculateDiscountWithMultipleGetProducts() {
            // Given: Add another get product
            getProducts.put(302L, 2); // Get 2 more products
            bxGyCoupon.setGetProducts(getProducts);
            // Total free quantity per application: 1 + 2 = 3
            // Can apply 2 times: 2 * 3 * 25.0 = 150.0

            // When
            double discount = bxGyCoupon.calculateDiscount(cartItems, productPrices);

            // Then
            assertEquals(150.0, discount, 0.01);
        }

        @Test
        @DisplayName("Should handle empty product prices")
        void testCalculateDiscountWithEmptyProductPrices() {
            // Given
            Map<Long, Double> emptyPrices = new HashMap<>();

            // When
            double discount = bxGyCoupon.calculateDiscount(cartItems, emptyPrices);

            // Then
            assertEquals(0.0, discount, 0.01); // No valid prices, so no discount
        }

        @Test
        @DisplayName("Should handle products with zero price")
        void testCalculateDiscountWithZeroPrices() {
            // Given
            Map<Long, Double> zeroPrices = new HashMap<>();
            zeroPrices.put(201L, 0.0);
            zeroPrices.put(202L, 0.0);

            // When
            double discount = bxGyCoupon.calculateDiscount(cartItems, zeroPrices);

            // Then
            assertEquals(0.0, discount, 0.01);
        }

        @Test
        @DisplayName("Should use cheapest buy product price")
        void testCalculateDiscountUsesCheapestPrice() {
            // Given: Product 201 costs 100.0, Product 202 costs 10.0 (cheaper)
            productPrices.put(201L, 100.0);
            productPrices.put(202L, 10.0); // This should be used as cheapest

            // When
            double discount = bxGyCoupon.calculateDiscount(cartItems, productPrices);

            // Then
            assertEquals(20.0, discount, 0.01); // 2 applications * 1 free * 10.0 = 20.0
        }
    }

    @Nested
    @DisplayName("CouponEntity Interface Implementation Tests")
    class CouponEntityImplementationTests {

        @Test
        @DisplayName("Should implement CouponEntity interface correctly")
        void testCouponEntityImplementation() {
            // Given
            CouponEntity couponEntity = bxGyCoupon;

            // When & Then
            assertEquals(bxGyCoupon.getCode(), couponEntity.getCode());
            assertEquals(bxGyCoupon.getExpirationDate(), couponEntity.getExpirationDate());
            assertEquals(bxGyCoupon.getIsActive(), couponEntity.getIsActive());
            assertEquals(bxGyCoupon.getDescription(), couponEntity.getDescription());

            // Test setters through interface
            couponEntity.setCode("NEW_BXGY_CODE");
            assertEquals("NEW_BXGY_CODE", bxGyCoupon.getCode());
        }
    }

    @Nested
    @DisplayName("Equals and HashCode Tests")
    class EqualsHashCodeTests {

        @Test
        @DisplayName("Should be equal when all properties are same")
        void testEqualsWhenSame() {
            // Given
            BxGyCoupon coupon1 = new BxGyCoupon();
            coupon1.setCode("SAME_CODE");
            coupon1.setRepetitionLimit(2);
            coupon1.setBuyProducts(Map.of(101L, 1));
            coupon1.setGetProducts(Map.of(201L, 1));

            BxGyCoupon coupon2 = new BxGyCoupon();
            coupon2.setCode("SAME_CODE");
            coupon2.setRepetitionLimit(2);
            coupon2.setBuyProducts(Map.of(101L, 1));
            coupon2.setGetProducts(Map.of(201L, 1));

            // When & Then
            assertEquals(coupon1, coupon2);
            assertEquals(coupon1.hashCode(), coupon2.hashCode());
        }

        @Test
        @DisplayName("Should not be equal when properties differ")
        void testNotEqualsWhenDifferent() {
            // Given
            BxGyCoupon coupon1 = new BxGyCoupon();
            coupon1.setCode("CODE1");
            coupon1.setRepetitionLimit(1);

            BxGyCoupon coupon2 = new BxGyCoupon();
            coupon2.setCode("CODE2");
            coupon2.setRepetitionLimit(2);

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
            String toString = bxGyCoupon.toString();

            // Then
            assertNotNull(toString);
            assertTrue(toString.contains("BUY2GET1"));
            assertTrue(toString.contains("BxGyCoupon"));
        }
    }

    @Nested
    @DisplayName("Edge Case Tests")
    class EdgeCaseTests {

        @Test
        @DisplayName("Should handle empty buy products map")
        void testWithEmptyBuyProducts() {
            // Given
            bxGyCoupon.setBuyProducts(new HashMap<>());

            // When
            boolean isApplicable = bxGyCoupon.isApplicable(cartItems);

            // Then
            assertTrue(isApplicable); // Empty buy products means no requirements
        }

        @Test
        @DisplayName("Should handle empty get products map")
        void testWithEmptyGetProducts() {
            // Given
            bxGyCoupon.setGetProducts(new HashMap<>());

            // When
            double discount = bxGyCoupon.calculateDiscount(cartItems, productPrices);

            // Then
            assertEquals(0.0, discount, 0.01); // No free products = no discount
        }

        @Test
        @DisplayName("Should handle very large repetition limit")
        void testWithVeryLargeRepetitionLimit() {
            // Given
            bxGyCoupon.setRepetitionLimit(Integer.MAX_VALUE);

            // When
            int times = bxGyCoupon.calculateApplicableTimes(cartItems);

            // Then
            assertEquals(2, times); // Still limited by product availability
        }

        @Test
        @DisplayName("Should handle zero repetition limit")
        void testWithZeroRepetitionLimit() {
            // Given
            bxGyCoupon.setRepetitionLimit(0);

            // When
            int times = bxGyCoupon.calculateApplicableTimes(cartItems);

            // Then
            assertEquals(0, times);
        }
    }
}
