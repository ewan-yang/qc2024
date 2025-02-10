package com.tecpie.platform.user.business.organization.api.command.update;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
public class PasswordUpdateCommand {

    @Schema(description = "登录名/账号")
    private String code;

    @Schema(description = "原密码")
    private String oldPass;

    @Schema(description = "新密码")
    private String newPass;

}
