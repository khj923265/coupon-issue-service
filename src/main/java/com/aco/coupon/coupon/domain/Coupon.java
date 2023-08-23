package com.aco.coupon.coupon.domain;

import com.aco.coupon.common.domain.BaseEntity;
import jakarta.persistence.Entity;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Entity
public class Coupon extends BaseEntity {
    private int count;
    private String name;

    public Coupon() {}

    public Coupon(int count, String name) {
        this.count = count;
        this.name = name;
    }

    @Builder
    public Coupon(Long id, LocalDateTime createdAt, boolean inValid, LocalDateTime invalidAt, int count, String name) {
        super(id, createdAt, inValid, invalidAt);
        this.count = count;
        this.name = name;
    }

    public void discount() {
        count = count - 1;
    }
}
