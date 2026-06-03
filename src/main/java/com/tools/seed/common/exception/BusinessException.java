package com.tools.seed.common.exception;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Schema(description = "业务异常")
@Getter
public class BusinessException extends RuntimeException {
    @Schema(description = "错误码")
    private final int code;

    public BusinessException(int code, String message) {
        super(message);
        this.code = code;
    }

    public BusinessException(String message) {
        this(500, message);
    }
}