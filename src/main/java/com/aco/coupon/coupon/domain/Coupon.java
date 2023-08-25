package com.aco.coupon.coupon.domain;

import com.aco.coupon.common.domain.BaseEntity;
import jakarta.persistence.Entity;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Entity
public class Coupon extends BaseEntity {
    private Long stock;
    private String name;

    public Coupon() {}

    public Coupon(Long stock, String name) {
        this.stock = stock;
        this.name = name;
    }

    @Builder
    public Coupon(Long id, LocalDateTime createdAt, boolean inValid, LocalDateTime invalidAt, Long stock, String name) {
        super(id, createdAt, inValid, invalidAt);
        this.stock = stock;
        this.name = name;
    }

    public void discount() {
        stock = stock - 1;
    }
}
