package com.aco.coupon.common.exception.base;

import com.aco.coupon.common.response.ApiResponseEntity;
import org.springframework.http.HttpStatus;

public abstract class BaseException extends RuntimeException{
    public abstract HttpStatus getStatus();
    public abstract String getErrorCode();
    public abstract String getErrorMessage();
    public abstract ApiResponseEntity<Void> getErrorResponse();

    @Override
    public String getMessage() {
        return getErrorMessage();
    }

    @Override
    public synchronized Throwable fillInStackTrace() {
        return this;
    }
}
