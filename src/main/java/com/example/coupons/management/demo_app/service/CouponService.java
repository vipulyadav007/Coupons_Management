package com.example.coupons.management.demo_app.service;

import com.example.coupons.management.demo_app.dto.*;
import com.example.coupons.management.demo_app.model.*;
import java.util.List;

public interface CouponService {

    CartWiseCoupon createCartWiseCoupon(CreateCartWiseCouponRequest request);

    ProductWiseCoupon createProductWiseCoupon(CreateProductWiseCouponRequest request);

    BxGyCoupon createBxGyCoupon(CreateBxGyCouponRequest request);

    List<CouponResponseDTO> getAllCoupons();

    CouponResponseDTO getCouponById(Long id);

    List<ApplicableCouponDTO> findApplicableCoupons(CartDTO cart);

    CouponApplicationResultDTO applyCoupon(Long couponId, CartDTO cart);
}
