package com.aco.coupon.coupon.controller;

import com.aco.coupon.common.response.ApiResponseEntity;
import com.aco.coupon.coupon.domain.Coupon;
import com.aco.coupon.coupon.domain.CouponHistory;
import com.aco.coupon.coupon.dto.CouponIssueDto;
import com.aco.coupon.coupon.service.CouponCommendService;
import com.aco.coupon.coupon.service.CouponQueryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "쿠폰", description = "쿠폰 API")
@RestController
@RequestMapping("/coupon")
public class CouponController {

    private final CouponCommendService couponCommendService;
    private final CouponQueryService couponQueryService;

    public CouponController(CouponCommendService couponCommendService, CouponQueryService couponQueryService) {
        this.couponCommendService = couponCommendService;
        this.couponQueryService = couponQueryService;
    }

    @Operation(summary = "쿠폰 등록", description = "쿠폰 등록 API")
    @PostMapping("")
    private ApiResponseEntity<Coupon> create(@RequestBody Coupon coupon) {
        couponCommendService.save(coupon);
        return ApiResponseEntity.ok(coupon);
    }

    @Operation(summary = "쿠폰 리스트 가져오기", description = "쿠폰 리스트 가져오기 API")
    @GetMapping("")
    private ApiResponseEntity<List<Coupon>> getList() {
        List<Coupon> couponList = couponQueryService.getCouponList();
        return ApiResponseEntity.ok(couponList);
    }

    @Operation(summary = "쿠폰 히스토리 리스트 가져오기", description = "쿠폰 히스토리 리스트 가져오기 API")
    @GetMapping("/history/{couponId}")
    private ApiResponseEntity<List<CouponHistory>> getHistoryList(@PathVariable Long couponId) {
        List<CouponHistory>  couponHistoryList = couponQueryService.getHistoryList(couponId);
        return ApiResponseEntity.ok(couponHistoryList);
    }

    @PostMapping("/issue")
    private ApiResponseEntity<String> issueCoupon(@Valid @RequestBody CouponIssueDto couponIssueDto) {
        couponCommendService.issueCoupon(couponIssueDto);
        return ApiResponseEntity.ok();
    }

    @PostMapping("/issuebyredisson")
    private ApiResponseEntity<String> issueCouponByRedisson(@Valid @RequestBody CouponIssueDto couponIssueDto) {
        couponCommendService.issueCouponByRedisson("coupon" + couponIssueDto.getCouponId() ,couponIssueDto);
        return ApiResponseEntity.ok();
    }

}
