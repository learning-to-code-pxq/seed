package com.tools.seed.common.vo;

import com.tools.seed.entity.SysUser;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.beans.BeanUtils;

// 用DTO隔离，永远不要把实体直接返回前端
@Schema(description = "用户信息")
@Data
public class UserVO {
    @Schema(description = "用户ID")
    private Long id;
    @Schema(description = "用户名")
    private String username;
    @Schema(description = "昵称")
    private String nickname;
    // 没有password，没有deleted

    public UserVO(SysUser user) {
        BeanUtils.copyProperties(user, this);
    }
}
