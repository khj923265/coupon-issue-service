package com.aco.coupon.coupon.service;

import com.aco.coupon.common.aop.DistributedLock;
import com.aco.coupon.common.exception.AlreadyIssueCouponException;
import com.aco.coupon.common.exception.CouponInvalidException;
import com.aco.coupon.common.exception.CouponIssueFailException;
import com.aco.coupon.coupon.domain.Coupon;
import com.aco.coupon.coupon.domain.CouponHistory;
import com.aco.coupon.coupon.domain.CouponRedis;
import com.aco.coupon.coupon.dto.CouponIssueDto;
import com.aco.coupon.coupon.repository.CouponHistoryRepository;
import com.aco.coupon.coupon.repository.CouponRedisRepository;
import com.aco.coupon.coupon.repository.CouponRepository;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class CouponCommendService {

    private final String REDISSON_LOCK_PREFIX = "LOCK:";
    private final int EMPTY = 0;
    private final RedissonClient redissonClient;
    private final CouponRepository couponRepository;
    private final CouponHistoryRepository couponHistoryRepository;

    private final CouponRedisRepository couponRedisRepository;

    public CouponCommendService(RedissonClient redissonClient, CouponRepository couponRepository, CouponHistoryRepository couponHistoryRepository, CouponRedisRepository couponRedisRepository) {
        this.redissonClient = redissonClient;
        this.couponRepository = couponRepository;
        this.couponHistoryRepository = couponHistoryRepository;
        this.couponRedisRepository = couponRedisRepository;
    }

    public void save(Coupon coupon) {
        Coupon savedCoupon = couponRepository.save(coupon);
//        redissonClient.getBucket("coupon" + ":" + savedCoupon.getId()).set(savedCoupon.getCount());
        CouponRedis couponRedis = CouponRedis.builder()
                .id(savedCoupon.getId())
                .name(savedCoupon.getName())
                .stock(savedCoupon.getStock())
                .build();
        couponRedisRepository.save(couponRedis);
    }

    @Transactional
    public void issueCoupon(CouponIssueDto couponIssueDto) {
        Coupon coupon = couponRepository.findWithPessimisticLockById(couponIssueDto.getCouponId())
                .orElseThrow(CouponIssueFailException::getInstance);
        if (coupon.isInValid() || coupon.getStock() <= 0) {
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

    @DistributedLock(key = "#lockName")
    public void issueCouponByRedisson(String lockName, CouponIssueDto couponIssueDto) {
        CouponRedis couponRedis = couponRedisRepository.findById(couponIssueDto.getCouponId())
                .orElseThrow(CouponIssueFailException::getInstance);

        if (couponRedis.getStock() <= EMPTY) {
            throw CouponInvalidException.getInstance();
        }

        if (couponRedis.getIssuedUserIdMap().containsKey(couponIssueDto.getMemberId())) {
            throw AlreadyIssueCouponException.getInstance();
        }

        couponRedis.addIssuedUser(couponIssueDto.getMemberId(), couponRedis.getStock());
        couponRedis.decrease();
        couponRedisRepository.save(couponRedis);

        //TODO 메세지 큐 방식으로 히스토리를 저장시키면 좋을듯?
        CouponHistory couponHistory = CouponHistory.builder()
                .couponId(couponIssueDto.getCouponId())
                .memberId(couponIssueDto.getMemberId())
                .build();

        couponHistoryRepository.save(couponHistory);

        log.info("현재 남은 재고 : {}개", couponRedis.getStock());
    }

}
