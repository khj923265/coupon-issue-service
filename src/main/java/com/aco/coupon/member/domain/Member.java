package com.aco.coupon.member.domain;

import com.aco.coupon.common.domain.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Entity
public class Member extends BaseEntity {

    @Column(unique = true, nullable = false)
    private String userId;
    private String name;

    public Member() {}

    public Member(String userId, String name) {
        this.userId = userId;
        this.name = name;
    }

    @Builder
    public Member(Long id, LocalDateTime createdAt, boolean inValid, LocalDateTime invalidAt, String userId, String name) {
        super(id, createdAt, inValid, invalidAt);
        this.userId = userId;
        this.name = name;
    }
}
