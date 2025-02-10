package com.tecpie.platform.user.security.access.controller;

import com.tecpie.library.common.business.controller.resource.Result;
import com.tecpie.library.common.util.assembler.TecpieAssemblerUtil;
import com.tecpie.platform.user.business.system.sys_operation_log.entity.OperationLog;
import com.tecpie.platform.user.business.system.sys_operation_log.service.OperationLogService;
import com.tecpie.starter.feign.entity.OperationLogSaveCommand;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.io.Serializable;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 获取用户信息
 *
 * @author tecpie
 */
@Slf4j
@Tag(name = "系统管理：用户管理接口")
@RestController
@RequestMapping("/cache/v1/operationLog")
public class OperationLogCacheController {

  @Autowired
  private OperationLogService operationLogService;

  /**
   * 存储操作日志
   *
   * @param operationLog 操作日志
   */
  @Operation(summary = "存储操作日志")
  @PostMapping
  public Result<Serializable> saveOperationLog(@RequestBody OperationLogSaveCommand operationLog) {
    return Result.success(
        operationLogService.saveOperationLog(TecpieAssemblerUtil.parse(operationLog,
            OperationLog.class)));
  }
}
