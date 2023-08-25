package com.aco.coupon.coupon.repository;

import com.aco.coupon.coupon.domain.CouponRedis;
import org.springframework.data.repository.CrudRepository;

public interface CouponRedisRepository extends CrudRepository<CouponRedis, Long> {
}
