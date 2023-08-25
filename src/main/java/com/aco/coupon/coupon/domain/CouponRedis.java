package com.aco.coupon.coupon.domain;

import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.redis.core.RedisHash;

import java.util.HashMap;

@Getter
@RedisHash(value = "coupon", timeToLive = 3600)
public class CouponRedis {

    @Id
    private Long id;

    private String name;

    private Long stock;

    private HashMap<Long, Long> issuedUserIdMap = new HashMap<>();

    public CouponRedis() {}

    @Builder
    public CouponRedis(Long id, String name, Long stock) {
        this.id = id;
        this.name = name;
        this.stock = stock;
    }

    public void decrease() {
        this.stock = this.stock - 1;
    }

    public void addIssuedUser(Long userId, Long count) {
        this.issuedUserIdMap.put(userId, count);
    }
}
