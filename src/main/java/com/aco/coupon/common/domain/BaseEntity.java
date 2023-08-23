package com.aco.coupon.common.domain;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Getter
@MappedSuperclass
public class BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Getter
    @CreationTimestamp
    private LocalDateTime createdAt;

    private boolean inValid;

    public BaseEntity() {}

    private LocalDateTime invalidAt;

    public void setInvalid(boolean inValid) {
        this.inValid = inValid;
        if(inValid) {
            invalidAt = LocalDateTime.now();
        } else {
            invalidAt = null;
        }
    }

    public BaseEntity(Long id, LocalDateTime createdAt, boolean inValid, LocalDateTime invalidAt) {
        this.id = id;
        this.createdAt = createdAt;
        this.inValid = inValid;
        this.invalidAt = invalidAt;
    }
}
