package com.tecpie.platform.user.security.access.controller;

import com.tecpie.library.common.business.controller.resource.Result;
import com.tecpie.platform.user.business.system.organization.service.SysUserService;
import com.tecpie.platform.user.business.system.organization.service.SysUserTokenService;
import com.tecpie.platform.user.utils.SecurityUtils;
import com.tecpie.starter.feign.entity.SecurityToken;
import com.tecpie.starter.feign.entity.SecurityUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.Serializable;

/**
 * 获取用户信息
 *
 * @author tecpie
 */
@Slf4j
@Tag(name = "系统管理：用户管理接口")
@RestController
@RequestMapping("/cache/v1/user")
public class UserCacheController {

  @Autowired
  private SysUserService sysUserService;

  @Autowired
  private SysUserTokenService sysUserTokenService;

  @Autowired
  private SecurityUtils securityUtils;

  @Operation(summary = "根据ID查询数据")
  @GetMapping("/{id}")
  public Result<SecurityUser> findById(@PathVariable Serializable id) {
    log.info("UserCacheController.findById()");
    return Result.success(sysUserService.selectSecurityUser(id));
  }

  @Operation(
      summary = "校验Token"
  )
  @PostMapping({"token/check"})
  public Result<Boolean> checkToken(@RequestBody SecurityToken securityToken) {
    return Result.success(this.sysUserTokenService.checkToken(securityToken.getToken()));
  }

  @Operation(
      summary = "更新Token"
  )
  @PostMapping({"/token"})
  public Result<String> createNewToken(@RequestBody SecurityToken securityToken) {
    return Result.success(this.securityUtils.generateNewToken(securityToken.getClient(), securityToken.getId(), securityToken.getToken()));
  }
}
