package com.example.coupons.management.demo_app.repository;

import com.example.coupons.management.demo_app.model.BxGyCoupon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface BxGyCouponRepository extends JpaRepository<BxGyCoupon, Long> {

    Optional<BxGyCoupon> findByCode(String code);

    @Query("SELECT c FROM BxGyCoupon c WHERE c.isActive = true AND c.expirationDate > :currentDate")
    List<BxGyCoupon> findAllActiveCoupons(LocalDate currentDate);
}
