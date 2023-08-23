package com.aco.coupon.common.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import java.util.StringJoiner;
import java.util.function.Supplier;

@ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "successful operation", content = @Content(schema = @Schema(implementation = ApiResponseEntity.class))),
        @ApiResponse(responseCode = "400", description = "bad request operation", content = @Content(schema = @Schema(implementation = ApiResponseEntity.class)))
})
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponseEntity<T> {
    private boolean success;
    private String code;
    private Supplier<T> result;
    private String message;

    public ApiResponseEntity(boolean success, String code, String message, Supplier<T> result) {
        this.success = success;
        this.code = code;
        this.message = message;
        this.result = result;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getCode() {
        return code;
    }

    public T getResult() {
        return result.get();
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", ApiResponseEntity.class.getSimpleName() + "[", "]")
                .add("success=" + success)
                .add("code='" + code + "'")
                .add("result=" + result)
                .add("message=" + message)
                .toString();
    }


    public static ApiResponseEntity<String> ok() {
        return ok("ok");
    }

    public static <T> ApiResponseEntity<T> ok(T result) {
        return new ApiResponseEntity<>(true, "200", "SUCCESS", () -> result);
    }

    public static <T> ApiResponseEntity<T> message(String message, T result) {
        return new ApiResponseEntity<>(true, "200", message, () -> result);
    }

    public static <T> ApiResponseEntity<T> fail(String code, T result) {
        return new ApiResponseEntity<>(false, code, "FAIL", () -> result);
    }

    public static <T> ApiResponseEntity<T> fail(int code, T result) {
        return new ApiResponseEntity<>(false, String.valueOf(code), "FAIL", () -> result);
    }

    public static <T> ApiResponseEntity<T> fail(String code, String message, T result) {
        return new ApiResponseEntity<>(false, code, message, () -> result);
    }

    public static <T> ApiResponseEntity<T> fail(int code, String message, T result) {
        return new ApiResponseEntity<>(false, String.valueOf(code), message, () -> result);
    }
}
