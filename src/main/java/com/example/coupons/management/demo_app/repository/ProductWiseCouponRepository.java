package com.example.coupons.management.demo_app.repository;

import com.example.coupons.management.demo_app.model.ProductWiseCoupon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProductWiseCouponRepository extends JpaRepository<ProductWiseCoupon, Long> {

    Optional<ProductWiseCoupon> findByCode(String code);

    @Query("SELECT c FROM ProductWiseCoupon c WHERE c.isActive = true AND c.expirationDate > :currentDate")
    List<ProductWiseCoupon> findAllActiveCoupons(LocalDate currentDate);

    List<ProductWiseCoupon> findByProductId(Long productId);
}
