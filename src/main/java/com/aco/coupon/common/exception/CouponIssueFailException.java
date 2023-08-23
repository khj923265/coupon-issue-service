package com.aco.coupon.common.exception;

import com.aco.coupon.common.exception.base.BaseException;
import com.aco.coupon.common.response.ApiResponseEntity;
import org.springframework.http.HttpStatus;

public class CouponIssueFailException extends BaseException{
    private static final HttpStatus STATUS = HttpStatus.BAD_REQUEST;
    private static final String MESSAGE = "쿠폰 발급에 실패했습니다.";
    private static final ApiResponseEntity<Void> RESPONSE = new ApiResponseEntity<>(false, String.valueOf(STATUS.value()), MESSAGE, () -> null);
    private static final CouponIssueFailException INSTANCE = new CouponIssueFailException();

    private CouponIssueFailException() {}

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
