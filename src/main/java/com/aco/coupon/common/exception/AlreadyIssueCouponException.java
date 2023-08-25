package com.aco.coupon.common.exception;

import com.aco.coupon.common.exception.base.BaseException;
import com.aco.coupon.common.response.ApiResponseEntity;
import org.springframework.http.HttpStatus;

public class AlreadyIssueCouponException extends BaseException{
    private static final HttpStatus STATUS = HttpStatus.BAD_REQUEST;
    private static final String MESSAGE = "이미 쿠폰을 발급 받았습니다.";
    private static final ApiResponseEntity<Void> RESPONSE = new ApiResponseEntity<>(false, String.valueOf(STATUS.value()), MESSAGE, () -> null);
    private static final AlreadyIssueCouponException INSTANCE = new AlreadyIssueCouponException();

    private AlreadyIssueCouponException() {}

    @Override
    public HttpStatus getStatus() {
        return STATUS;
    }

    @Override
    public String getErrorCode() {
        return String.valueOf(STATUS.value());
    }

    @Override
    public String getErrorMessage() {
        return MESSAGE;
    }

    @Override
    public ApiResponseEntity<Void> getErrorResponse() {
        return RESPONSE;
    }

    public static BaseException getInstance() {
        return INSTANCE;
    }
}
