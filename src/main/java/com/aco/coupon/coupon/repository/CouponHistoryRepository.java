package com.aco.coupon.coupon.repository;

import com.aco.coupon.coupon.domain.CouponHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CouponHistoryRepository extends JpaRepository<CouponHistory, Long> {
    List<CouponHistory> findByCouponId(Long couponId);
    boolean existsByCouponIdAndMemberId(Long couponId, Long memberId);
}
