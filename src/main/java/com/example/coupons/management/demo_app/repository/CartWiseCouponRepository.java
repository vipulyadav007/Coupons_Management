package com.example.coupons.management.demo_app.repository;

import com.example.coupons.management.demo_app.model.CartWiseCoupon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface CartWiseCouponRepository extends JpaRepository<CartWiseCoupon, Long> {

    Optional<CartWiseCoupon> findByCode(String code);

    @Query("SELECT c FROM CartWiseCoupon c WHERE c.isActive = true AND c.expirationDate > :currentDate")
    List<CartWiseCoupon> findAllActiveCoupons(LocalDate currentDate);
}
