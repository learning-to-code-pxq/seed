package com.tools.seed.common.result;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "统一API响应结果")
@Data
public class Result<T> {
    @Schema(description = "状态码", example = "200")
    private int code;
    @Schema(description = "消息", example = "success")
    private String message;
    @Schema(description = "数据")
    private T data;

    private Result() {}

    @Schema(description = "成功返回结果,包含数据")
    public static <T> Result<T> success(T data) {
        Result<T> r = new Result<>();
        r.setCode(200);
        r.setMessage("success");
        r.setData(data);
        return r;
    }

    @Schema(description = "成功返回结果,不包含数据")
    public static <T> Result<T> success() {
        return success(null);
    }

    @Schema(description = "失败返回结果,包含错误码和消息")
    public static <T> Result<T> fail(int code, String message) {
        Result<T> r = new Result<>();
        r.setCode(code);
        r.setMessage(message);
        return r;
    }

    @Schema(description = "失败返回结果,包含消息,默认错误码500")
    public static <T> Result<T> fail(String message) {
        return fail(500, message);
    }
}