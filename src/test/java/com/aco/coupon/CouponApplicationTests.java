package com.aco.coupon;

import com.aco.coupon.coupon.domain.Coupon;
import com.aco.coupon.coupon.domain.CouponHistory;
import com.aco.coupon.coupon.dto.CouponIssueDto;
import com.aco.coupon.coupon.service.CouponCommendService;
import com.aco.coupon.coupon.service.CouponQueryService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.IntStream;

@Slf4j
@DisplayName("선착순 쿠폰 발급 테스트")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CouponApplicationTests {

    @Autowired
    private CouponCommendService couponCommendService;

    @Autowired
    private CouponQueryService couponQueryService;

    @Test
    void saveCoupon() {
        Coupon coupon = Coupon.builder()
                .stock(100L)
                .name("테스트쿠폰")
                .build();

        couponCommendService.save(coupon);
    }

    @Test
    @Transactional
    void RDB쿠폰발급테스트() {
        Long couponId = 15L;

        List<CouponIssueDto> list = IntStream.rangeClosed(1, 200)
                .mapToObj(i -> new CouponIssueDto(couponId, (long) i))
                .toList();

//        for (CouponIssueDto coupon : list) {
//            couponCommendService.issueCoupon(coupon);
//        }

        ExecutorService executor = Executors.newFixedThreadPool(10); // 10개의 스레드를 가진 풀을 생성
        for (CouponIssueDto coupon : list) {
            executor.submit(() -> couponCommendService.issueCoupon(coupon));
        }

        executor.shutdown(); // 모든 작업이 완료되면 스레드 풀을 종료
    }

    @Test
    void 레디스를사용한쿠폰발급테스트() {
        Long couponId = 17L;

        List<CouponIssueDto> list = IntStream.rangeClosed(1, 1000)
                .mapToObj(i -> new CouponIssueDto(couponId, (long) i))
                .toList();

        for (CouponIssueDto coupon : list) {
            couponCommendService.issueCouponByRedisson("coupon" + couponId, coupon);
        }

        ExecutorService executor = Executors.newFixedThreadPool(10); // 10개의 스레드를 가진 풀을 생성
        for (CouponIssueDto coupon : list) {
            executor.submit(() -> couponCommendService.issueCouponByRedisson("coupon" + couponId, coupon));
        }

        executor.shutdown(); // 모든 작업이 완료되면 스레드 풀을 종료
    }

}
