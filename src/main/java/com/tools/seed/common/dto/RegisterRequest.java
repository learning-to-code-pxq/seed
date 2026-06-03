package com.tools.seed.common.dto;

import com.tools.seed.common.validator.PasswordStrength;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Schema(description = "注册请求")
@Data
public class RegisterRequest {
    @Schema(description = "用户名", example = "admin")
    @NotBlank(message = "用户名不能为空")
    @Size(min = 3, max = 20, message = "用户名3-20位")
    private String username;

    @Schema(description = "密码", example = "123456")
    @NotBlank(message = "密码不能为空")
    @PasswordStrength
    private String password;

    @Schema(description = "昵称", example = "管理员")
    private String nickname;
}
