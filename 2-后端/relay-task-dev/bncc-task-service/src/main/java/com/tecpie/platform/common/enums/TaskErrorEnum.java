package com.tecpie.platform.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import com.tecpie.library.common.exception.IEnumerableError;
import lombok.Getter;

/**
 * 社区异常举类
 *
 * @author aoqin.huang
 * @version v1.0.0
 * @date 2022/08/02
 */
@Getter
public enum TaskErrorEnum implements IEnumerableError {

    // 未配置项目审批流程
    APPROVE_CONFIG_NOT_EXIST(200002, "当前项目类型未配置审批流程，请联系管理员");

  @EnumValue
  @JsonValue
  private final Integer code;

  private final String message;

  TaskErrorEnum(Integer code, String message) {
    this.code = code;
    this.message = message;
  }

}
