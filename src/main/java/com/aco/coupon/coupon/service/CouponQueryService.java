package com.aco.coupon.coupon.service;

import com.aco.coupon.coupon.domain.Coupon;
import com.aco.coupon.coupon.domain.CouponHistory;
import com.aco.coupon.coupon.repository.CouponHistoryRepository;
import com.aco.coupon.coupon.repository.CouponRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class CouponQueryService {

    private final CouponRepository couponRepository;
    private final CouponHistoryRepository couponHistoryRepository;

    public CouponQueryService(CouponRepository couponRepository, CouponHistoryRepository couponHistoryRepository) {
        this.couponRepository = couponRepository;
        this.couponHistoryRepository = couponHistoryRepository;
    }

    public List<Coupon> getCouponList() {
        return couponRepository.findAll();
    }

    public List<CouponHistory> getHistoryList(Long couponId) {
        return couponHistoryRepository.findByCouponId(couponId);
    }
}
