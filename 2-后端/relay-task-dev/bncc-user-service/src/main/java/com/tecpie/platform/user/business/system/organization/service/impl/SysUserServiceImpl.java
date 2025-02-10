package com.tecpie.platform.user.business.system.organization.service.impl;

import com.google.common.collect.Maps;
import com.tecpie.library.common.business.entity.BaseEntity;
import com.tecpie.library.common.exception.BusinessException;
import com.tecpie.library.common.exception.SystemError;
import com.tecpie.platform.user.business.organization.api.command.query.SysUserQueryCommand;
import com.tecpie.platform.user.business.organization.api.command.update.PasswordUpdateCommand;
import com.tecpie.platform.user.business.system.organization.controller.assembler.command.SysUserToSysUserRoleAssemble;
import com.tecpie.platform.user.business.system.organization.controller.assembler.command.SysUserToSysUserUnitAssemble;
import com.tecpie.platform.user.business.system.organization.entity.SysUser;
import com.tecpie.platform.user.business.system.organization.entity.SysUserRole;
import com.tecpie.platform.user.business.system.organization.entity.SysUserUnit;
import com.tecpie.platform.user.business.system.organization.mapper.SysUserMapper;
import com.tecpie.platform.user.business.system.organization.service.SysUserRoleService;
import com.tecpie.platform.user.business.system.organization.service.SysUserService;
import com.tecpie.platform.user.business.system.organization.service.SysUserUnitService;
import com.tecpie.starter.feign.entity.SecurityUser;
import com.tecpie.starter.jdbc.support.mybatis.business.service.impl.BaseServiceImpl;
import com.tecpie.starter.security.support.cache.UserCacheHelper;
import com.tecpie.starter.security.support.util.TecpieSecurityUtil;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

/**
 * 用户信息表 服务类实现
 *
 * @author tecpie
 * @since 2022-11-09
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class SysUserServiceImpl extends BaseServiceImpl<SysUserMapper, SysUser> implements
        SysUserService {

    private static final String NON_EXIST_MESSAGE = "用户信息表[%s]不存在";

    private final SysUserRoleService sysUserRoleService;

    private final SysUserUnitService sysUserUnitService;

    @Autowired
    public SysUserServiceImpl(SysUserRoleService sysUserRoleService,
                              SysUserUnitService sysUserUnitService) {
        this.sysUserRoleService = sysUserRoleService;
        this.sysUserUnitService = sysUserUnitService;
    }


    /**
     * 在本类中只重载了父类的 {@link BaseServiceImpl#logicRemoveById(Serializable)}方法，重载的原因是因为逻辑删除 Lov 时，需将其所属的
     * LovLine 同步逻辑删除 当前仅在{@link com.tecpie.platform.user.business.system.organization.controller.SysUserRestController removeSysUserById(Serializable)}方法中使用了该方法
     * 如使用其他逻辑删除的方法，需在本类中重载相应的方法
     *
     * @param id
     * @return
     */
    @Override
    public boolean logicRemoveById(Serializable id) {
        SysUser existEntity = this.baseMapper.selectExtensionById(id);
        if (existEntity == null) {
            throw BusinessException
                    .build(SystemError.NO_SUCH_OBJECT_ERROR, String.format(NON_EXIST_MESSAGE, id));
        }

        // 逻辑删除用户与角色、用户与组织机构的关联
        sysUserRoleService.lambdaUpdate().eq(SysUserRole::getUserId, id).set(BaseEntity::getDeleted, 1)
                .update();
        sysUserUnitService.lambdaUpdate().eq(SysUserUnit::getUserId, id).set(BaseEntity::getDeleted, 1)
                .update();
        return super.logicRemoveById(id);
    }

    @Override
    public SysUser selectExtensionById(Serializable id) {
        SysUser entity = this.baseMapper.selectExtensionById(id);
        if (entity == null) {
            throw BusinessException
                    .build(SystemError.NO_SUCH_OBJECT_ERROR, String.format(NON_EXIST_MESSAGE, id));
        }
        return entity;
    }

    @Override
    public SecurityUser selectSecurityUser(Serializable id) {
        SecurityUser securityUser = this.baseMapper.selectSecurityUser(id);
        SysUser sysUser = baseMapper.selectById(id);
        Map<String, Object> extendData = Maps.newHashMap();
        extendData.put("engineersTeamId", sysUser.getEngineersTeamId());
        securityUser.setExtendData(extendData);
        return securityUser;
    }

    @Override
    public List<SysUser> searchExtensionPageList(SysUserQueryCommand command) {
        return this.baseMapper.searchExtensionPageList(command);
    }

    @Override
    public SysUser selectAuthByCode(String code) {
        return this.baseMapper.selectAuthByCode(code);
    }

    @Override
    public Serializable saveSysUser(SysUser entity) {
        entity.setPasswordDate(LocalDateTime.now());
        entity.setEntryDate(LocalDateTime.now());
        this.save(entity);
        if (!CollectionUtils.isEmpty(entity.getRoleList())) {
            sysUserRoleService.saveBatch(SysUserToSysUserRoleAssemble.parse(entity));
        }
        if (!CollectionUtils.isEmpty(entity.getUnitList())) {
            sysUserUnitService.saveBatch(SysUserToSysUserUnitAssemble.parse(entity));
        }
        return entity.getId();
    }

    @Override
    public void updateSysUser(Serializable id, SysUser entity) {
        SysUser existEntity = this.baseMapper.selectExtensionById(id);
        if (existEntity == null) {
            throw BusinessException
                    .build(SystemError.NO_SUCH_OBJECT_ERROR, String.format(NON_EXIST_MESSAGE, id));
        }
        entity.setId(id);
        Optional.ofNullable(entity.getPassword())
                .ifPresent(pwd -> entity.setPasswordDate(LocalDateTime.now()));
        this.updateById(entity);
        if (entity.getRoleList() != null) {
            sysUserRoleService.lambdaUpdate().eq(SysUserRole::getUserId, id).remove();
            sysUserRoleService.saveBatch(SysUserToSysUserRoleAssemble.parse(entity));
        }
        if (entity.getUnitList() != null) {
            sysUserUnitService.lambdaUpdate().eq(SysUserUnit::getUserId, id).remove();
            sysUserUnitService.saveBatch(SysUserToSysUserUnitAssemble.parse(entity));
        }
        UserCacheHelper.removeSecurityUser(id.toString());
    }

    @Override
    public void changeSysUserStatus(Serializable id, Integer status) {
        boolean result = this.lambdaUpdate()
                .set(SysUser::getStatus, status)
                .set(SysUser::getUpdateBy, TecpieSecurityUtil.getUser().getId())
                .set(SysUser::getUpdateDate, LocalDateTime.now())
                .eq(SysUser::getId, id).update();

        if (!result) {
            throw BusinessException
                    .build(SystemError.NO_SUCH_OBJECT_ERROR, String.format(NON_EXIST_MESSAGE, id));
        }
        UserCacheHelper.removeSecurityUser(id.toString());
    }

    @Override
    public List<SysUser> searchListByUnit(SysUserQueryCommand condition) {
        return this.baseMapper.searchListByUnit(condition);
    }

    @Override
    public List<SysUser> getUserListByIds(List<Long> ids) {
        return this.baseMapper.selectByIds(ids);
    }

    @Override
    public void updatePassword(PasswordUpdateCommand command) {
        String code = command.getCode();
        String newPass = command.getNewPass();
        if (StringUtils.isEmpty(newPass)){
            throw BusinessException.build("新密码不允许为空");
        }
        SysUser user = this.lambdaQuery().eq(SysUser::getCode, code).one();
        if (ObjectUtils.isEmpty(user)) {
            throw BusinessException.build("用户不存在");
        }
        // 匹配原密码是否一致
        String oldPass = command.getOldPass();
        if (StringUtils.isEmpty(oldPass) || !oldPass.equals(user.getPassword())){
            throw BusinessException.build("密码错误");
        }
        this.lambdaUpdate()
                .eq(SysUser::getCode, code).set(SysUser::getPassword, newPass)
                .set(SysUser::getPasswordDate, LocalDateTime.now())
                .update();
    }

}