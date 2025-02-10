package com.tecpie.platform.task_user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tecpie.platform.task_user.entity.TaskUserBackupPhone;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 停电任务用户备用联系方式表 数据映射接口
 *
 * @author zhijie.tan
 * @since 2023-08-08
 */
@Repository
public interface TaskUserBackupPhoneMapper extends BaseMapper<TaskUserBackupPhone> {

    /**
     * 根据户号获取备用联系方式
     *
     * @param accountNumber 户号
     * @return String
     */
    String searchByAccountNumber(@Param("accountNumber") String accountNumber);

    /**
     * 根据户号List获取备用联系方式
     *
     * @param accountNumberList 户号List
     * @return List<TaskUserBackupPhone>
     */
    List<TaskUserBackupPhone> searchListByAccountNumber(@Param("accountNumberList") List<String> accountNumberList);

}