package com.tools.seed.common.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Schema(description = "登录响应")
@Data
@AllArgsConstructor
public class LoginResponse {
    @Schema(description = "访问令牌")
    private String token;
    @Schema(description = "用户名")
    private String username;
    @Schema(description = "用户ID")
    private Long userId;
}
