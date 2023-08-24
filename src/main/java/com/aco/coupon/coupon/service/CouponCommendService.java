package com.aco.coupon.coupon.service;

import com.aco.coupon.common.exception.AlreadyIssueCouponException;
import com.aco.coupon.common.exception.CouponInvalidException;
import com.aco.coupon.common.exception.CouponIssueFailException;
import com.aco.coupon.coupon.domain.Coupon;
import com.aco.coupon.coupon.domain.CouponHistory;
import com.aco.coupon.coupon.dto.CouponIssueDto;
import com.aco.coupon.coupon.repository.CouponHistoryRepository;
import com.aco.coupon.coupon.repository.CouponRepository;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBucket;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@Transactional
public class CouponCommendService {

    private final String REDISSON_LOCK_PREFIX = "LOCK:";
    private final int ZERO = 0;
    private final RedissonClient redissonClient;
    private final CouponRepository couponRepository;
    private final CouponHistoryRepository couponHistoryRepository;

    public CouponCommendService(RedissonClient redissonClient, CouponRepository couponRepository, CouponHistoryRepository couponHistoryRepository) {
        this.redissonClient = redissonClient;
        this.couponRepository = couponRepository;
        this.couponHistoryRepository = couponHistoryRepository;
    }

    public void save(Coupon coupon) {
        Coupon savedCoupon = couponRepository.save(coupon);
        redissonClient.getBucket("coupon" + ":" + savedCoupon.getId()).set(savedCoupon.getCount());
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

    public void issueCouponByRedisson(CouponIssueDto couponIssueDto) {
        String key = "coupon" + ":" + couponIssueDto.getCouponId();
        String lockKey = key + ":lock";
        final RLock rLock = redissonClient.getLock(lockKey);

        String threadNumber = Thread.currentThread().getName();

        //TODO 같은 유저가 한번에 여러번 요청시 redis 에서 거르질 못하고 있음
        // 해당부분 때문에 RDB 값 카운트도 안맞음 redis count 에만 맞춰져 있음
        // 해결해야함

        try {
            if(!rLock.tryLock(1, 3, TimeUnit.SECONDS)) {
                throw CouponIssueFailException.getInstance();
            }

            final int couponCount = getCurrentCouponCount(key);
            if(couponCount <= ZERO){
                log.info("[{}] 현재 남은 재고가 없습니다. ({}개)", threadNumber , couponCount);
                throw CouponInvalidException.getInstance();
            }

            log.info("threadNumber : {} & 현재 남은 재고 : {}개", threadNumber, couponCount - 1);
            couponDecrease(key, couponCount);
        } catch (InterruptedException e) {
            log.info(e.getMessage());
            throw CouponIssueFailException.getInstance();
        } finally {
            try {
                rLock.unlock();
            } catch (IllegalMonitorStateException e) {
                log.info("Redisson Lock Already UnLock issueCouponByRedisson {}", key);
            }
        }

        Coupon coupon = couponRepository.findById(couponIssueDto.getCouponId())
                .orElseThrow(CouponIssueFailException::getInstance);
        CouponHistory couponHistory = CouponHistory.builder()
                .couponId(couponIssueDto.getCouponId())
                .memberId(couponIssueDto.getMemberId())
                .build();

        couponHistoryRepository.save(couponHistory);
        coupon.discount();
    }

    private int getCurrentCouponCount(String key) {
        RBucket<Object> bucket = redissonClient.getBucket(key);
        if (bucket != null && bucket.isExists()) {
            Object value = bucket.get();
            if (value instanceof Integer) {
                return (int) value;
            } else {
                log.warn("Redisson key 조회 결과 값이 Integer 형식이 아닙니다. 반환된 값: {}", value);
                throw new IllegalStateException("쿠폰 카운트 형식이 올바르지 않습니다.");
            }
        } else {
            log.warn("Redisson에서 key({}) 조회 결과 값이 존재하지 않습니다.", key);
            throw new IllegalStateException("쿠폰 키가 존재하지 않습니다.");
        }
    }

    private void couponDecrease(String key, int couponCount) {
        redissonClient.getBucket(key).set(couponCount - 1);
    }

}
