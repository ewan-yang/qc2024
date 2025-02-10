package com.tecpie.platform.task_user.service.impl;

import com.google.common.collect.Maps;
import com.tecpie.platform.task_user.entity.TaskUserBackupPhone;
import com.tecpie.platform.task_user.mapper.TaskUserBackupPhoneMapper;
import com.tecpie.platform.task_user.service.TaskUserBackupPhoneService;
import com.tecpie.starter.jdbc.support.mybatis.business.service.impl.BaseServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 停电任务用户备用联系方式表 服务类实现
 *
 * @author zhijie.tan
 * @since 2023-08-08
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class TaskUserBackupPhoneServiceImpl extends BaseServiceImpl<TaskUserBackupPhoneMapper, TaskUserBackupPhone> implements TaskUserBackupPhoneService {

    @Override
    public String searchByAccountNumber(String accountNumber) {
        return this.baseMapper.searchByAccountNumber(accountNumber);
    }

    @Override
    public Map<String, String> searchListByAccountNumber(List<String> accountNumberList) {
        Map<String, String> resultMap = Maps.newHashMap();
        List<TaskUserBackupPhone> backupPhoneList = this.baseMapper.searchListByAccountNumber(accountNumberList);
        if (!CollectionUtils.isEmpty(backupPhoneList)) {
            backupPhoneList.forEach(e -> resultMap.put(e.getAccountNumber(), e.getBackupPhone()));
        }
        return resultMap;
    }

    @Override
    public Serializable saveTaskUserBackupPhone(TaskUserBackupPhone entity) {
        // 先查询数据是否存在
        TaskUserBackupPhone taskUserBackupPhone = this.lambdaQuery().eq(TaskUserBackupPhone::getAccountNumber, entity.getAccountNumber()).one();
        if (taskUserBackupPhone == null) {
            this.save(entity);
        } else {
            entity.setId(taskUserBackupPhone.getId());
            this.updateById(entity);
        }
        return entity.getId();
    }

}