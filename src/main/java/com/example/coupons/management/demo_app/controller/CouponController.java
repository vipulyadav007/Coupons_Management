package com.example.coupons.management.demo_app.controller;

import com.example.coupons.management.demo_app.dto.*;
import com.example.coupons.management.demo_app.model.*;
import com.example.coupons.management.demo_app.service.CouponService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/coupons")
@RequiredArgsConstructor
@Slf4j
@Validated
public class CouponController {

    private final CouponService couponService;

    @PostMapping("/cart-wise")
    public ResponseEntity<CartWiseCoupon> createCartWiseCoupon(
            @Valid @RequestBody CreateCartWiseCouponRequest request) {
        log.info("Creating cart-wise coupon with code: {}", request.getCode());
        CartWiseCoupon coupon = couponService.createCartWiseCoupon(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(coupon);
    }

    @PostMapping("/product-wise")
    public ResponseEntity<ProductWiseCoupon> createProductWiseCoupon(
            @Valid @RequestBody CreateProductWiseCouponRequest request) {
        log.info("Creating product-wise coupon with code: {}", request.getCode());
        ProductWiseCoupon coupon = couponService.createProductWiseCoupon(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(coupon);
    }

    @PostMapping("/bxgy")
    public ResponseEntity<BxGyCoupon> createBxGyCoupon(
            @Valid @RequestBody CreateBxGyCouponRequest request) {
        log.info("Creating BxGy coupon with code: {}", request.getCode());
        BxGyCoupon coupon = couponService.createBxGyCoupon(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(coupon);
    }

    @GetMapping
    public ResponseEntity<List<CouponResponseDTO>> getAllCoupons() {
        log.info("Retrieving all coupons");
        List<CouponResponseDTO> coupons = couponService.getAllCoupons();
        return ResponseEntity.ok(coupons);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CouponResponseDTO> getCouponById(@PathVariable Long id) {
        log.info("Retrieving coupon with id: {}", id);
        CouponResponseDTO coupon = couponService.getCouponById(id);
        return ResponseEntity.ok(coupon);
    }

    @PostMapping("/applicable-coupons")
    public ResponseEntity<List<ApplicableCouponDTO>> getApplicableCoupons(
            @Valid @RequestBody CartDTO cart) {
        log.info("Finding applicable coupons for cart with {} items", cart.getItems().size());
        List<ApplicableCouponDTO> applicableCoupons = couponService.findApplicableCoupons(cart);
        return ResponseEntity.ok(applicableCoupons);
    }

    @PostMapping("/apply-coupon/{id}")
    public ResponseEntity<CouponApplicationResultDTO> applyCoupon(
            @PathVariable Long id,
            @Valid @RequestBody CartDTO cart) {
        log.info("Applying coupon with id: {} to cart", id);
        CouponApplicationResultDTO result = couponService.applyCoupon(id, cart);
        return ResponseEntity.ok(result);
    }
}
