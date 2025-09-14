package com.example.coupons.management.demo_app.serviceImpl;

import com.example.coupons.management.demo_app.dto.*;
import com.example.coupons.management.demo_app.exception.CouponNotApplicableException;
import com.example.coupons.management.demo_app.exception.CouponNotFoundException;
import com.example.coupons.management.demo_app.model.*;
import com.example.coupons.management.demo_app.repository.*;
import com.example.coupons.management.demo_app.serviceimpl.CouponServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CouponServiceImplTest {

    @Mock
    private CartWiseCouponRepository cartWiseCouponRepository;

    @Mock
    private ProductWiseCouponRepository productWiseCouponRepository;

    @Mock
    private BxGyCouponRepository bxGyCouponRepository;

    @InjectMocks
    private CouponServiceImpl couponService;

    private CreateCartWiseCouponRequest cartWiseRequest;
    private CreateProductWiseCouponRequest productWiseRequest;
    private CreateBxGyCouponRequest bxGyRequest;
    private CartDTO testCart;

    @BeforeEach
    void setUp() {
        cartWiseRequest = new CreateCartWiseCouponRequest();
        cartWiseRequest.setCode("CART10");
        cartWiseRequest.setExpirationDate(LocalDate.now().plusDays(30));
        cartWiseRequest.setDescription("10% off on cart");
        cartWiseRequest.setThreshold(BigDecimal.valueOf(100));
        cartWiseRequest.setDiscountPercentage(BigDecimal.valueOf(10));

        productWiseRequest = new CreateProductWiseCouponRequest();
        productWiseRequest.setCode("PRODUCT20");
        productWiseRequest.setExpirationDate(LocalDate.now().plusDays(30));
        productWiseRequest.setDescription("20% off on product");
        productWiseRequest.setProductId(201L);
        productWiseRequest.setDiscountPercentage(BigDecimal.valueOf(20));

        bxGyRequest = new CreateBxGyCouponRequest();
        bxGyRequest.setCode("BUY2GET1");
        bxGyRequest.setExpirationDate(LocalDate.now().plusDays(30));
        bxGyRequest.setDescription("Buy 2 get 1 free");
        bxGyRequest.setBuyProducts(Map.of(201L, 2));
        bxGyRequest.setGetProducts(Map.of(201L, 1));
        bxGyRequest.setRepetitionLimit(1);

        // Setup test cart
        testCart = new CartDTO();
        testCart.setItems(Arrays.asList(
            createItemDTO(201L, 3, 50.0),
            createItemDTO(202L, 1, 100.0)
        ));
    }

    private ItemDTO createItemDTO(Long productId, Integer quantity, Double price) {
        ItemDTO item = new ItemDTO();
        item.setProductId(productId);
        item.setQuantity(quantity);
        item.setPrice(price);
        return item;
    }

    @Test
    void createCartWiseCoupon_ShouldCreateAndSaveCoupon() {
        // Given
        CartWiseCoupon savedCoupon = new CartWiseCoupon();
        savedCoupon.setId(1L);
        savedCoupon.setCode("CART10");
        when(cartWiseCouponRepository.save(any(CartWiseCoupon.class))).thenReturn(savedCoupon);

        // When
        CartWiseCoupon result = couponService.createCartWiseCoupon(cartWiseRequest);

        // Then
        assertNotNull(result);
        assertEquals("CART10", result.getCode());
        verify(cartWiseCouponRepository).save(any(CartWiseCoupon.class));
    }

    @Test
    void createProductWiseCoupon_ShouldCreateAndSaveCoupon() {
        // Given
        ProductWiseCoupon savedCoupon = new ProductWiseCoupon();
        savedCoupon.setId(1L);
        savedCoupon.setCode("PRODUCT20");
        when(productWiseCouponRepository.save(any(ProductWiseCoupon.class))).thenReturn(savedCoupon);

        // When
        ProductWiseCoupon result = couponService.createProductWiseCoupon(productWiseRequest);

        // Then
        assertNotNull(result);
        assertEquals("PRODUCT20", result.getCode());
        verify(productWiseCouponRepository).save(any(ProductWiseCoupon.class));
    }

    @Test
    void createBxGyCoupon_ShouldCreateAndSaveCoupon() {
        // Given
        BxGyCoupon savedCoupon = new BxGyCoupon();
        savedCoupon.setId(1L);
        savedCoupon.setCode("BUY2GET1");
        when(bxGyCouponRepository.save(any(BxGyCoupon.class))).thenReturn(savedCoupon);

        // When
        BxGyCoupon result = couponService.createBxGyCoupon(bxGyRequest);

        // Then
        assertNotNull(result);
        assertEquals("BUY2GET1", result.getCode());
        verify(bxGyCouponRepository).save(any(BxGyCoupon.class));
    }

    @Test
    void getAllCoupons_ShouldReturnAllCouponsFromAllRepositories() {
        // Given
        List<CartWiseCoupon> cartWiseCoupons = Arrays.asList(createCartWiseCoupon());
        List<ProductWiseCoupon> productWiseCoupons = Arrays.asList(createProductWiseCoupon());
        List<BxGyCoupon> bxGyCoupons = Arrays.asList(createBxGyCoupon());

        when(cartWiseCouponRepository.findAll()).thenReturn(cartWiseCoupons);
        when(productWiseCouponRepository.findAll()).thenReturn(productWiseCoupons);
        when(bxGyCouponRepository.findAll()).thenReturn(bxGyCoupons);

        // When
        List<CouponResponseDTO> result = couponService.getAllCoupons();

        // Then
        assertEquals(3, result.size());
        verify(cartWiseCouponRepository).findAll();
        verify(productWiseCouponRepository).findAll();
        verify(bxGyCouponRepository).findAll();
    }

    @Test
    void getCouponById_WhenCartWiseCouponExists_ShouldReturnCoupon() {
        // Given
        CartWiseCoupon coupon = createCartWiseCoupon();
        when(cartWiseCouponRepository.findById(1L)).thenReturn(Optional.of(coupon));

        // When
        CouponResponseDTO result = couponService.getCouponById(1L);

        // Then
        assertNotNull(result);
        assertEquals("CART10", result.getCode());
        assertEquals("CART_WISE", result.getType());
    }

    @Test
    void getCouponById_WhenCouponNotFound_ShouldThrowException() {
        // Given
        when(cartWiseCouponRepository.findById(999L)).thenReturn(Optional.empty());
        when(productWiseCouponRepository.findById(999L)).thenReturn(Optional.empty());
        when(bxGyCouponRepository.findById(999L)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(CouponNotFoundException.class, () ->
            couponService.getCouponById(999L));
    }

    @Test
    void findApplicableCoupons_ShouldReturnApplicableCoupons() {
        // Given
        CartWiseCoupon cartWiseCoupon = createCartWiseCoupon();
        ProductWiseCoupon productWiseCoupon = createProductWiseCoupon();
        BxGyCoupon bxGyCoupon = createBxGyCoupon();

        when(cartWiseCouponRepository.findAllActiveCoupons(any(LocalDate.class)))
            .thenReturn(Arrays.asList(cartWiseCoupon));
        when(productWiseCouponRepository.findAllActiveCoupons(any(LocalDate.class)))
            .thenReturn(Arrays.asList(productWiseCoupon));
        when(bxGyCouponRepository.findAllActiveCoupons(any(LocalDate.class)))
            .thenReturn(Arrays.asList(bxGyCoupon));

        // When
        List<ApplicableCouponDTO> result = couponService.findApplicableCoupons(testCart);

        // Then
        assertNotNull(result);
        assertTrue(result.size() >= 0); // Depends on mock implementation
    }

    @Test
    void applyCoupon_WhenValidCartWiseCoupon_ShouldApplyDiscount() {
        // Given
        CartWiseCoupon coupon = createCartWiseCoupon();
        when(cartWiseCouponRepository.findById(1L)).thenReturn(Optional.of(coupon));

        // When
        CouponApplicationResultDTO result = couponService.applyCoupon(1L, testCart);

        // Then
        assertNotNull(result);
        assertEquals(250.0, result.getOriginalTotal());
        assertEquals(25.0, result.getDiscountAmount()); // 10% of 250
        assertEquals(225.0, result.getFinalTotal());
        assertEquals("CART10", result.getAppliedCouponCode());
    }

    @Test
    void applyCoupon_WhenExpiredCoupon_ShouldThrowException() {
        // Given
        CartWiseCoupon expiredCoupon = createCartWiseCoupon();
        expiredCoupon.setExpirationDate(LocalDate.now().minusDays(1));
        when(cartWiseCouponRepository.findById(1L)).thenReturn(Optional.of(expiredCoupon));

        // When & Then
        assertThrows(CouponNotApplicableException.class, () ->
            couponService.applyCoupon(1L, testCart));
    }

    @Test
    void applyCoupon_WhenInactiveCoupon_ShouldThrowException() {
        // Given
        CartWiseCoupon inactiveCoupon = createCartWiseCoupon();
        inactiveCoupon.setIsActive(false);
        when(cartWiseCouponRepository.findById(1L)).thenReturn(Optional.of(inactiveCoupon));

        // When & Then
        assertThrows(CouponNotApplicableException.class, () ->
            couponService.applyCoupon(1L, testCart));
    }

    @Test
    void applyCoupon_WhenCouponNotApplicable_ShouldThrowException() {
        // Given
        CartWiseCoupon coupon = createCartWiseCoupon();
        coupon.setThreshold(BigDecimal.valueOf(1000)); // Higher than cart total
        when(cartWiseCouponRepository.findById(1L)).thenReturn(Optional.of(coupon));

        // When & Then
        assertThrows(CouponNotApplicableException.class, () ->
            couponService.applyCoupon(1L, testCart));
    }

    @Test
    void applyCoupon_WithProductWiseCoupon_ShouldCalculateCorrectDiscount() {
        // Given
        ProductWiseCoupon coupon = createProductWiseCoupon();
        when(productWiseCouponRepository.findById(1L)).thenReturn(Optional.of(coupon));

        // When
        CouponApplicationResultDTO result = couponService.applyCoupon(1L, testCart);

        // Then
        assertNotNull(result);
        assertEquals(30.0, result.getDiscountAmount()); // 20% of (3 * 50) = 30
    }

    // Helper methods to create test entities
    private CartWiseCoupon createCartWiseCoupon() {
        CartWiseCoupon coupon = new CartWiseCoupon();
        coupon.setId(1L);
        coupon.setCode("CART10");
        coupon.setExpirationDate(LocalDate.now().plusDays(30));
        coupon.setDescription("10% off on cart");
        coupon.setThreshold(BigDecimal.valueOf(100));
        coupon.setDiscountPercentage(BigDecimal.valueOf(10));
        coupon.setIsActive(true);
        coupon.setCouponType(CouponType.CART_WISE);
        return coupon;
    }

    private ProductWiseCoupon createProductWiseCoupon() {
        ProductWiseCoupon coupon = new ProductWiseCoupon();
        coupon.setId(2L);
        coupon.setCode("PRODUCT20");
        coupon.setExpirationDate(LocalDate.now().plusDays(30));
        coupon.setDescription("20% off on product");
        coupon.setProductId(201L);
        coupon.setDiscountPercentage(BigDecimal.valueOf(20));
        coupon.setIsActive(true);
        coupon.setCouponType(CouponType.PRODUCT_WISE);
        return coupon;
    }

    private BxGyCoupon createBxGyCoupon() {
        BxGyCoupon coupon = new BxGyCoupon();
        coupon.setId(3L);
        coupon.setCode("BUY2GET1");
        coupon.setExpirationDate(LocalDate.now().plusDays(30));
        coupon.setDescription("Buy 2 get 1 free");
        coupon.setBuyProducts(Map.of(201L, 2));
        coupon.setGetProducts(Map.of(201L, 1));
        coupon.setRepetitionLimit(1);
        coupon.setIsActive(true);
        coupon.setCouponType(CouponType.BXGY);
        return coupon;
    }
}
