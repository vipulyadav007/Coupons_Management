package com.example.coupons.management.demo_app.model;

import java.time.LocalDate;

public interface CouponEntity {
    Long getId();
    String getCode();
    LocalDate getExpirationDate();
    Boolean getIsActive();
    String getDescription();
    void setCode(String code);
    void setExpirationDate(LocalDate expirationDate);
    void setIsActive(Boolean isActive);
    void setDescription(String description);
}
