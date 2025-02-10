package com.tecpie.platform.user.business.system.organization.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tecpie.platform.user.business.system.organization.entity.SysUserToken;
import com.tecpie.platform.user.business.system.organization.sys_user_token.api.command.query.SysUserTokenQueryCommand;
import java.io.Serializable;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * 用户Token表 数据映射接口
 *
 * @author tecpie
 * @since 2023-12-06
 */
@Repository
public interface SysUserTokenMapper extends BaseMapper<SysUserToken> {

  /**
   * 获取详情信息
   *
   * @param id
   * @return SysUserToken
   */
  SysUserToken selectExtensionById(@Param("id") Serializable id);


  /**
   * 检索详情列表
   *
   * @param command
   * @return List<SysUserToken>
   */
  List<SysUserToken> searchExtensionPageList(@Param("condition") SysUserTokenQueryCommand command);

  SysUserToken selectForUpdate(@Param("token") String token);
}