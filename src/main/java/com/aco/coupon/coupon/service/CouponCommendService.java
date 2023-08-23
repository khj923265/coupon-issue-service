package com.aco.coupon.coupon.service;

import com.aco.coupon.common.exception.AlreadyIssueCouponException;
import com.aco.coupon.common.exception.CouponInvalidException;
import com.aco.coupon.common.exception.CouponIssueFailException;
import com.aco.coupon.coupon.domain.Coupon;
import com.aco.coupon.coupon.domain.CouponHistory;
import com.aco.coupon.coupon.dto.CouponIssueDto;
import com.aco.coupon.coupon.repository.CouponHistoryRepository;
import com.aco.coupon.coupon.repository.CouponRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class CouponCommendService {

    private final CouponRepository couponRepository;
    private final CouponHistoryRepository couponHistoryRepository;

    public CouponCommendService(CouponRepository couponRepository, CouponHistoryRepository couponHistoryRepository) {
        this.couponRepository = couponRepository;
        this.couponHistoryRepository = couponHistoryRepository;
    }

    public void save(Coupon coupon) {
        couponRepository.save(coupon);
    }

    public void saveCouponHistory() {

    }

    public void issueCoupon(CouponIssueDto couponIssueDto) {
        Coupon coupon = couponRepository.findWithPessimisticLockById(couponIssueDto.getCouponId())
                .orElseThrow(CouponIssueFailException::getInstance);
        if (coupon.isInValid() || coupon.getCount() <= 0) {
            throw CouponInvalidException.getInstance();
        }

        boolean hasCoupon = couponHistoryRepository.existsByCouponIdAndMemberId(couponIssueDto.getCouponId(), couponIssueDto.getMemberId());
        if (hasCoupon) {
            throw AlreadyIssueCouponException.getInstance();
        }

        CouponHistory couponHistory = CouponHistory.builder()
                .couponId(couponIssueDto.getCouponId())
                .memberId(couponIssueDto.getMemberId())
                .build();

        couponHistoryRepository.save(couponHistory);
        coupon.discount();
    }
}
