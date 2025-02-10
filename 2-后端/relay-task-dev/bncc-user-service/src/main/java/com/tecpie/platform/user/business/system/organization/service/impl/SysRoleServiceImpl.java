package com.tecpie.platform.user.business.system.organization.service.impl;

import com.tecpie.library.common.business.entity.BaseEntity;
import com.tecpie.library.common.exception.BusinessException;
import com.tecpie.library.common.exception.SystemError;
import com.tecpie.platform.user.business.organization.api.command.query.SysRoleQueryCommand;
import com.tecpie.platform.user.business.system.organization.controller.assembler.command.SysRoleToSysRolePermissionAssemble;
import com.tecpie.platform.user.business.system.organization.controller.assembler.command.SysRoleToSysUserRoleAssemble;
import com.tecpie.platform.user.business.system.organization.entity.SysRole;
import com.tecpie.platform.user.business.system.organization.entity.SysRolePermission;
import com.tecpie.platform.user.business.system.organization.entity.SysUserRole;
import com.tecpie.platform.user.business.system.organization.mapper.SysRoleMapper;
import com.tecpie.platform.user.business.system.organization.service.SysRolePermissionService;
import com.tecpie.platform.user.business.system.organization.service.SysRoleService;
import com.tecpie.platform.user.business.system.organization.service.SysUserRoleService;
import com.tecpie.starter.jdbc.support.mybatis.business.service.impl.BaseServiceImpl;
import com.tecpie.starter.security.support.util.TecpieSecurityUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 角色信息表 服务类实现
 *
 * @author tecpie
 * @since 2022-11-09
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class SysRoleServiceImpl extends BaseServiceImpl<SysRoleMapper, SysRole> implements SysRoleService {

    @Autowired
    private SysUserRoleService sysUserRoleService;

    @Autowired
    private SysRolePermissionService sysRolePermissionService;

    private static final String NON_EXIST_MESSAGE = "角色信息表[%s]不存在";

    /**
     * 在本类中只重载了父类的 {@link BaseServiceImpl#logicRemoveById(Serializable)}方法，重载的原因是因为逻辑删除 Lov 时，需将其所属的 LovLine 同步逻辑删除
     * 当前仅在{@link com.tecpie.platform.user.business.system.organization.controller.SysRoleRestController#removeSysRoleById(Serializable)}方法中使用了该方法
     * 如使用其他逻辑删除的方法，需在本类中重载相应的方法
     *
     * @param id
     * @return
     */
    @Override
    public boolean logicRemoveById(Serializable id) {
        SysRole existEntity = this.baseMapper.selectExtensionById(id);
        if (existEntity == null) {
            throw BusinessException
                    .build(SystemError.NO_SUCH_OBJECT_ERROR, String.format(NON_EXIST_MESSAGE, id));
        }

        // 逻辑删除角色与用户、角色与权限的关联
        sysUserRoleService.lambdaUpdate().eq(SysUserRole::getRoleId, id).set(BaseEntity::getDeleted, 1)
                .update();
        sysRolePermissionService.lambdaUpdate().eq(SysRolePermission::getRoleId, id)
                .set(BaseEntity::getDeleted, 1).update();
        return super.logicRemoveById(id);
    }

    @Override
    public SysRole selectExtensionById(Serializable id) {
        SysRole entity = this.baseMapper.selectExtensionById(id);
        if (entity == null) {
            throw BusinessException.build(SystemError.NO_SUCH_OBJECT_ERROR, String.format(NON_EXIST_MESSAGE, id));
        }
        return entity;
    }

    @Override
    public List<SysRole> searchExtensionPageList(SysRoleQueryCommand command) {
        return this.baseMapper.searchExtensionPageList(command);
    }

    @Override
    public Serializable saveSysRole(SysRole entity) {
        this.save(entity);
        sysUserRoleService.saveBatch(SysRoleToSysUserRoleAssemble.parse(entity));
        sysRolePermissionService.saveBatch(SysRoleToSysRolePermissionAssemble.parse(entity));
        return entity.getId();
    }

    @Override
    public void updateSysRole(Serializable id, SysRole entity) {
        SysRole existEntity = this.baseMapper.selectExtensionById(id);
        if (existEntity == null) {
            throw BusinessException
                    .build(SystemError.NO_SUCH_OBJECT_ERROR, String.format(NON_EXIST_MESSAGE, id));
        }
        entity.setId(id);
        this.updateById(entity);
        if (entity.getUserList() != null) {
            sysUserRoleService.lambdaUpdate().eq(SysUserRole::getRoleId, id).remove();
            sysUserRoleService.saveBatch(SysRoleToSysUserRoleAssemble.parse(entity));
        }
        if (entity.getPermissionList() != null) {
            sysRolePermissionService.lambdaUpdate().eq(SysRolePermission::getRoleId, id).remove();
            sysRolePermissionService.saveBatch(SysRoleToSysRolePermissionAssemble.parse(entity));
        }
    }

    @Override
    public void changeSysRoleStatus(Serializable id, Integer status) {
        boolean result = this.lambdaUpdate()
                .set(SysRole::getStatus, status)
                .set(SysRole::getUpdateBy, TecpieSecurityUtil.getUser().getId())
                .set(SysRole::getUpdateDate, LocalDateTime.now())
                .eq(SysRole::getId, id).update();

        if (!result) {
            throw BusinessException.build(SystemError.NO_SUCH_OBJECT_ERROR, String.format(NON_EXIST_MESSAGE, id));
        }
    }

    @Override
    public List<SysRole> listByUserIdList(List<Serializable> userIdList) {
        return baseMapper.listByUserIdList(userIdList);
    }

}