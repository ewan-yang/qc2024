package com.tecpie.platform.task_user.service;

import com.tecpie.platform.task_user.entity.TaskUserBackupPhone;
import com.tecpie.platform.task_user.mapper.TaskUserBackupPhoneMapper;
import com.tecpie.starter.jdbc.support.mybatis.business.service.IBaseService;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 停电任务用户备用联系方式表 服务类接口
 *
 * @author zhijie.tan
 * @since 2023-08-08
 */
public interface TaskUserBackupPhoneService extends IBaseService<TaskUserBackupPhoneMapper, TaskUserBackupPhone> {

    /**
     * 根据户号获取备用联系方式
     *
     * @param accountNumber 户号
     * @return String
     */
    String searchByAccountNumber(String accountNumber);

    /**
     * 根据户号List获取备用联系方式
     *
     * @param accountNumberList 户号List
     * @return Map<String, String>
     */
    Map<String, String> searchListByAccountNumber(List<String> accountNumberList);

    /**
     * 保存
     *
     * @param entity
     * @return Serializable
     */
    Serializable saveTaskUserBackupPhone(TaskUserBackupPhone entity);

}