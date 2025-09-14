package com.example.coupons.management.demo_app.serviceimpl;

import com.example.coupons.management.demo_app.dto.*;
import com.example.coupons.management.demo_app.model.*;
import com.example.coupons.management.demo_app.repository.*;
import com.example.coupons.management.demo_app.exception.CouponNotFoundException;
import com.example.coupons.management.demo_app.exception.CouponNotApplicableException;
import com.example.coupons.management.demo_app.service.CouponService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class CouponServiceImpl implements CouponService {

    private final CartWiseCouponRepository cartWiseCouponRepository;
    private final ProductWiseCouponRepository productWiseCouponRepository;
    private final BxGyCouponRepository bxGyCouponRepository;

    @Override
    public CartWiseCoupon createCartWiseCoupon(CreateCartWiseCouponRequest request) {
        CartWiseCoupon coupon = new CartWiseCoupon();
        coupon.setCode(request.getCode());
        coupon.setExpirationDate(request.getExpirationDate());
        coupon.setDescription(request.getDescription());
        coupon.setThreshold(request.getThreshold());
        coupon.setDiscountPercentage(request.getDiscountPercentage());
        coupon.setIsActive(true);
        coupon.setCouponType(CouponType.CART_WISE); // Explicitly set the coupon type

        log.info("Creating cart-wise coupon with code: {}", coupon.getCode());
        return cartWiseCouponRepository.save(coupon);
    }

    @Override
    public ProductWiseCoupon createProductWiseCoupon(CreateProductWiseCouponRequest request) {
        ProductWiseCoupon coupon = new ProductWiseCoupon();
        coupon.setCode(request.getCode());
        coupon.setExpirationDate(request.getExpirationDate());
        coupon.setDescription(request.getDescription());
        coupon.setProductId(request.getProductId());
        coupon.setDiscountPercentage(request.getDiscountPercentage());
        coupon.setIsActive(true);
        coupon.setCouponType(CouponType.PRODUCT_WISE); // Explicitly set the coupon type

        log.info("Creating product-wise coupon with code: {}", coupon.getCode());
        return productWiseCouponRepository.save(coupon);
    }

    @Override
    public BxGyCoupon createBxGyCoupon(CreateBxGyCouponRequest request) {
        BxGyCoupon coupon = new BxGyCoupon();
        coupon.setCode(request.getCode());
        coupon.setExpirationDate(request.getExpirationDate());
        coupon.setDescription(request.getDescription());
        coupon.setBuyProducts(request.getBuyProducts());
        coupon.setGetProducts(request.getGetProducts());
        coupon.setRepetitionLimit(request.getRepetitionLimit());
        coupon.setIsActive(true);
        coupon.setCouponType(CouponType.BXGY);

        log.info("Creating BxGy coupon with code: {}", coupon.getCode());
        return bxGyCouponRepository.save(coupon);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CouponResponseDTO> getAllCoupons() {
        List<CouponResponseDTO> allCoupons = new ArrayList<>();

        // Get all cart-wise coupons
        List<CartWiseCoupon> cartWiseCoupons = cartWiseCouponRepository.findAll();
        allCoupons.addAll(cartWiseCoupons.stream()
            .map(this::convertToDto)
            .collect(Collectors.toList()));

        // Get all product-wise coupons
        List<ProductWiseCoupon> productWiseCoupons = productWiseCouponRepository.findAll();
        allCoupons.addAll(productWiseCoupons.stream()
            .map(this::convertToDto)
            .collect(Collectors.toList()));

        // Get all BxGy coupons
        List<BxGyCoupon> bxGyCoupons = bxGyCouponRepository.findAll();
        allCoupons.addAll(bxGyCoupons.stream()
            .map(this::convertToDto)
            .collect(Collectors.toList()));

        return allCoupons;
    }

    @Override
    @Transactional(readOnly = true)
    public CouponResponseDTO getCouponById(Long id) {
        // Try to find the coupon in each repository
        Optional<CartWiseCoupon> cartWise = cartWiseCouponRepository.findById(id);
        if (cartWise.isPresent()) {
            return convertToDto(cartWise.get());
        }

        Optional<ProductWiseCoupon> productWise = productWiseCouponRepository.findById(id);
        if (productWise.isPresent()) {
            return convertToDto(productWise.get());
        }

        Optional<BxGyCoupon> bxGy = bxGyCouponRepository.findById(id);
        if (bxGy.isPresent()) {
            return convertToDto(bxGy.get());
        }

        throw new CouponNotFoundException("Coupon not found with id: " + id);
    }

    // Helper methods to convert entities to DTOs
    private CouponResponseDTO convertToDto(CartWiseCoupon coupon) {
        CouponResponseDTO dto = new CouponResponseDTO();
        dto.setId(coupon.getId());
        dto.setCode(coupon.getCode());
        dto.setExpirationDate(coupon.getExpirationDate());
        dto.setIsActive(coupon.getIsActive());
        dto.setDescription(coupon.getDescription());
        dto.setType("CART_WISE");
        dto.setThreshold(coupon.getThreshold().doubleValue());
        dto.setDiscountPercentage(coupon.getDiscountPercentage().doubleValue());
        return dto;
    }

    private CouponResponseDTO convertToDto(ProductWiseCoupon coupon) {
        CouponResponseDTO dto = new CouponResponseDTO();
        dto.setId(coupon.getId());
        dto.setCode(coupon.getCode());
        dto.setExpirationDate(coupon.getExpirationDate());
        dto.setIsActive(coupon.getIsActive());
        dto.setDescription(coupon.getDescription());
        dto.setType("PRODUCT_WISE");
        dto.setProductId(coupon.getProductId());
        dto.setDiscountPercentage(coupon.getDiscountPercentage().doubleValue());
        return dto;
    }

    private CouponResponseDTO convertToDto(BxGyCoupon coupon) {
        CouponResponseDTO dto = new CouponResponseDTO();
        dto.setId(coupon.getId());
        dto.setCode(coupon.getCode());
        dto.setExpirationDate(coupon.getExpirationDate());
        dto.setIsActive(coupon.getIsActive());
        dto.setDescription(coupon.getDescription());
        dto.setType("BXGY");
        dto.setRepetitionLimit(coupon.getRepetitionLimit());
        return dto;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ApplicableCouponDTO> findApplicableCoupons(CartDTO cart) {
        log.info("Finding applicable coupons for cart with {} items", cart.getItems().size());
        List<ApplicableCouponDTO> applicableCoupons = new ArrayList<>();
        LocalDate now = LocalDate.now();

        // Check cart-wise coupons
        List<CartWiseCoupon> activeCartWiseCoupons = cartWiseCouponRepository.findAllActiveCoupons(now);
        for (CartWiseCoupon coupon : activeCartWiseCoupons) {
            double discount = coupon.calculateDiscount(cart.getTotalValue());
            if (discount > 0) {
                ApplicableCouponDTO dto = new ApplicableCouponDTO(
                    coupon.getId(),
                    coupon.getCode(),
                    "CART_WISE",
                    coupon.getDescription(),
                    discount
                );
                applicableCoupons.add(dto);
            }
        }

        // Check product-wise coupons
        List<ProductWiseCoupon> activeProductWiseCoupons = productWiseCouponRepository.findAllActiveCoupons(now);
        for (ProductWiseCoupon coupon : activeProductWiseCoupons) {
            double discount = cart.getItems().stream()
                .filter(item -> item.getProductId().equals(coupon.getProductId()))
                .mapToDouble(item -> coupon.calculateDiscount(
                    item.getProductId(),
                    item.getQuantity(),
                    item.getPrice()))
                .sum();

            if (discount > 0) {
                ApplicableCouponDTO dto = new ApplicableCouponDTO(
                    coupon.getId(),
                    coupon.getCode(),
                    "PRODUCT_WISE",
                    coupon.getDescription(),
                    discount
                );
                applicableCoupons.add(dto);
            }
        }

        // Check BxGy coupons
        List<BxGyCoupon> activeBxGyCoupons = bxGyCouponRepository.findAllActiveCoupons(now);
        Map<Long, Integer> cartItems = cart.getItems().stream()
                .collect(Collectors.toMap(
                    ItemDTO::getProductId,
                    ItemDTO::getQuantity,
                    Integer::sum));

        Map<Long, Double> productPrices = cart.getItems().stream()
                .collect(Collectors.toMap(
                    ItemDTO::getProductId,
                    ItemDTO::getPrice,
                    (existing, replacement) -> existing));

        for (BxGyCoupon coupon : activeBxGyCoupons) {
            // Check if coupon is applicable first (regardless of discount amount)
            if (coupon.isApplicable(cartItems)) {
                double discount = coupon.calculateDiscount(cartItems, productPrices);
                ApplicableCouponDTO dto = new ApplicableCouponDTO(
                    coupon.getId(),
                    coupon.getCode(),
                    "BXGY",
                    coupon.getDescription(),
                    discount // This might be 0 if free product prices are unknown, but coupon is still applicable
                );
                applicableCoupons.add(dto);
            }
        }

        log.info("Found {} applicable coupons", applicableCoupons.size());
        return applicableCoupons;
    }

    @Override
    @Transactional(readOnly = true)
    public CouponApplicationResultDTO applyCoupon(Long couponId, CartDTO cart) {
        log.info("Applying coupon with id: {} to cart", couponId);

        CouponResponseDTO couponDto = getCouponById(couponId);

        // Verify coupon is active and not expired
        if (!couponDto.getIsActive() || couponDto.getExpirationDate().isBefore(LocalDate.now())) {
            throw new CouponNotApplicableException("Coupon is inactive or expired");
        }

        double discountAmount = calculateDiscountForCoupon(couponDto, cart);

        if (discountAmount == 0) {
            throw new CouponNotApplicableException("Coupon is not applicable to this cart");
        }

        double originalTotal = cart.getTotalValue();
        double finalTotal = Math.max(0, originalTotal - discountAmount);

        CouponApplicationResultDTO result = new CouponApplicationResultDTO();
        result.setUpdatedItems(new ArrayList<>(cart.getItems()));
        result.setOriginalTotal(originalTotal);
        result.setDiscountAmount(discountAmount);
        result.setFinalTotal(finalTotal);
        result.setAppliedCouponCode(couponDto.getCode());
        result.setMessage("Coupon applied successfully");

        log.info("Coupon applied. Original: {}, Discount: {}, Final: {}",
                originalTotal, discountAmount, finalTotal);

        return result;
    }

    private double calculateDiscountForCoupon(CouponResponseDTO couponDto, CartDTO cart) {
        return switch (couponDto.getType()) {
            case "CART_WISE" -> {
                if (cart.getTotalValue() >= couponDto.getThreshold()) {
                    yield cart.getTotalValue() * (couponDto.getDiscountPercentage() / 100.0);
                }
                yield 0.0;
            }
            case "PRODUCT_WISE" -> cart.getItems().stream()
                .filter(item -> item.getProductId().equals(couponDto.getProductId()))
                .mapToDouble(item -> (item.getQuantity() * item.getPrice()) * (couponDto.getDiscountPercentage() / 100.0))
                .sum();
            case "BXGY" -> {
                // For BXGY, we need to get the actual coupon entity
                Optional<BxGyCoupon> bxGyCoupon = bxGyCouponRepository.findById(couponDto.getId());
                if (bxGyCoupon.isPresent()) {
                    Map<Long, Integer> cartItems = cart.getItems().stream()
                            .collect(Collectors.toMap(
                                ItemDTO::getProductId,
                                ItemDTO::getQuantity,
                                Integer::sum));

                    Map<Long, Double> productPrices = cart.getItems().stream()
                            .collect(Collectors.toMap(
                                ItemDTO::getProductId,
                                ItemDTO::getPrice,
                                (existing, replacement) -> existing));

                    yield bxGyCoupon.get().calculateDiscount(cartItems, productPrices);
                }
                yield 0.0;
            }
            default -> 0.0;
        };
    }
}
