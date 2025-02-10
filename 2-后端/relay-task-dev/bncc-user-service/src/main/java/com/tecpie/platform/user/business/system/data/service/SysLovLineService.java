package com.tecpie.platform.user.business.system.data.service;

import com.tecpie.platform.user.business.data.api.command.query.SysLovLineQueryCommand;
import com.tecpie.platform.user.business.system.data.entity.SysLovLine;
import com.tecpie.platform.user.business.system.data.mapper.SysLovLineMapper;
import com.tecpie.starter.jdbc.support.mybatis.business.service.IBaseService;

import java.io.Serializable;
import java.util.List;

/**
 * LOV明细行 服务类接口
 *
 * @author tecpie
 * @since 2022-11-09
 */
public interface SysLovLineService extends IBaseService<SysLovLineMapper, SysLovLine> {

  /**
   * 获取详情信息
   *
   * @param id
   * @return SysLovLine
   */
  SysLovLine selectExtensionById(Serializable id);

  /**
   * 检索详情列表
   *
   * @param command
   * @return List<SysLovLine>
   */
  List<SysLovLine> searchExtensionPageList(SysLovLineQueryCommand command);

  /**
   * 保存
   *
   * @param entity
   * @return Serializable
   */
  Serializable saveSysLovLine(SysLovLine entity);

  /**
   * 更新
   *
   * @param id
   * @param entity
   */
  void updateSysLovLine(Serializable id, SysLovLine entity);

  /**
   * 变更状态
   *
   * @param id
   * @param status
   */
  void changeSysLovLineStatus(Serializable id, Integer status);

}