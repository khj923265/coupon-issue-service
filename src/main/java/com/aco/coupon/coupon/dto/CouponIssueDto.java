package com.aco.coupon.coupon.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class CouponIssueDto {
    @NotNull
    private Long couponId;
    @NotNull
    private Long memberId;

    public CouponIssueDto() {
    }

    public CouponIssueDto(Long couponId, Long memberId) {
        this.couponId = couponId;
        this.memberId = memberId;
    }
}
