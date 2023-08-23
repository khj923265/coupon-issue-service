package com.aco.coupon.coupon.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Getter
@Entity
public class CouponHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long couponId;
    private Long memberId;
    @Getter
    @CreationTimestamp
    private LocalDateTime createdAt;

    public CouponHistory() {}

    @Builder
    public CouponHistory(Long id, Long couponId, Long memberId, LocalDateTime createdAt) {
        this.id = id;
        this.couponId = couponId;
        this.memberId = memberId;
        this.createdAt = createdAt;
    }
}
