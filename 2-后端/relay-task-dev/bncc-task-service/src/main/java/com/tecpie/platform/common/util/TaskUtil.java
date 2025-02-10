package com.tecpie.platform.common.util;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateUtil;
import com.google.common.collect.Maps;
import com.tecpie.library.common.exception.BusinessException;
import com.tecpie.library.common.exception.SystemError;
import com.tecpie.platform.common.constants.TaskConstants;
import com.tecpie.platform.common.enums.*;
import com.tecpie.platform.task.entity.Task;
import com.tecpie.platform.task.entity.TaskExecuteStatus;
import com.tecpie.platform.task_user.entity.TaskUser;
import com.tecpie.platform.user.business.system.data.entity.SysLov;
import com.tecpie.platform.user.business.system.data.entity.SysLovLine;
import com.tecpie.starter.security.support.util.TecpieSecurityUtil;
import org.apache.commons.compress.utils.Lists;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * 停电通知任务Util类
 * <p>
 * Copyright (C) TECPIE, All rights reserved.
 *
 * @author zhijie.tan
 * @date 2023/6/29 16:42
 */
public class TaskUtil {

    /**
     * 计算相关的数量
     *
     * @param entity             停电任务对象
     * @param isEngineersTeamCal 是否根据工程队计算
     */
    public static void calQty(Task entity, boolean isEngineersTeamCal) {
        // 停电通知用户List
        List<TaskUser> taskUserList = entity.getTaskUserList();
        if (CollectionUtils.isEmpty(taskUserList)) {
            return;
        }
        // 统计清0
        entity.setUnassignedQty(0);
        entity.setAssignedQty(entity.getUnassignedQty());
        entity.setCancelledQty(entity.getUnassignedQty());
        entity.setUnsignedQty(entity.getUnassignedQty());
        entity.setAgreedQty(entity.getUnassignedQty());
        entity.setRejectedQty(entity.getUnassignedQty());
        entity.setCancelUnassignedQty(entity.getUnassignedQty());
        entity.setCancelAssignedQty(entity.getUnassignedQty());
        entity.setCancelUnsignedQty(entity.getUnassignedQty());
        entity.setCancelAgreedQty(entity.getUnassignedQty());
        // 工程队List
        List<String> engineersTeamIdList = Lists.newArrayList();
        // 当前用户所属工程队
        Serializable currUserEngineersTeamId = getEngineersTeamId();
        // 循环任务用户
        for (TaskUser taskUser : taskUserList) {
            // 工程队
            Serializable engineersTeamId = taskUser.getEngineersTeamId();
            if (ObjectUtils.isNotEmpty(engineersTeamId)) {
                // 如果根据工程队计算
                if (isEngineersTeamCal && !ObjectUtils.isEmpty(currUserEngineersTeamId)) {
                    if (!engineersTeamId.toString().equals(currUserEngineersTeamId.toString())) {
                        continue;
                    }
                }
                if (!engineersTeamIdList.contains(engineersTeamId.toString())) {
                    engineersTeamIdList.add(engineersTeamId.toString());
                }
            }
            // 派发状态
            String assignStatus = taskUser.getAssignStatus();
            // 未派发数量
            if (StringUtils.isBlank(assignStatus) || TaskUserAssignStatusEnum.WPF.getCode().equals(assignStatus)) {
                entity.setUnassignedQty(getNewValue(entity.getUnassignedQty()));
            }
            if (StringUtils.isNotBlank(assignStatus)) {
                // 已派发数量
                if (TaskUserAssignStatusEnum.YPF.getCode().equals(assignStatus) || TaskUserAssignStatusEnum.YFK.getCode().equals(assignStatus)) {
                    entity.setAssignedQty(getNewValue(entity.getAssignedQty()));
                }
                // 不派发数量
                else if (TaskUserAssignStatusEnum.BPF.getCode().equals(assignStatus)) {
                    entity.setCancelledQty(getNewValue(entity.getCancelledQty()));
                }
            }
            // 反馈状态
            String feedbackStatus = taskUser.getFeedbackStatus();
            // 未签数量, 如果是不派发，则不统计未签数量
            if (!TaskUserAssignStatusEnum.BPF.getCode().equals(assignStatus) && !TaskUserAssignStatusEnum.WPF.getCode().equals(assignStatus) && (StringUtils.isBlank(feedbackStatus) || TaskUserFeedbackStatusEnum.WQ.getCode().equals(feedbackStatus))) {
                entity.setUnsignedQty(getNewValue(entity.getUnsignedQty()));
            }
            if (StringUtils.isNotBlank(feedbackStatus)) {
                // 同意数量
                if (TaskUserFeedbackStatusEnum.TY.getCode().equals(feedbackStatus)) {
                    entity.setAgreedQty(getNewValue(entity.getAgreedQty()));
                }
                // 拒签数量
                else if (TaskUserFeedbackStatusEnum.JQ.getCode().equals(feedbackStatus)) {
                    entity.setRejectedQty(getNewValue(entity.getRejectedQty()));
                }
            }
            // 取消通知派发状态
            String cancelAssignStatus = taskUser.getCancelAssignStatus();
            if (StringUtils.isNotBlank(cancelAssignStatus)) {
                // 未派发数量
                if (TaskUserCancelAssignStatusEnum.WPF.getCode().equals(cancelAssignStatus)) {
                    entity.setCancelUnassignedQty(getNewValue(entity.getCancelUnassignedQty()));
                }
                // 已派发数量
                else if (TaskUserCancelAssignStatusEnum.YPF.getCode().equals(cancelAssignStatus) || TaskUserCancelAssignStatusEnum.YFK.getCode().equals(cancelAssignStatus)) {
                    entity.setCancelAssignedQty(getNewValue(entity.getCancelAssignedQty()));
                }
            }
            // 取消通知反馈状态
            String cancelFeedbackStatus = taskUser.getCancelFeedbackStatus();
            if (StringUtils.isNotBlank(cancelFeedbackStatus)) {
                // 同意数量
                if (TaskUserCancelFeedbackStatusEnum.TY.getCode().equals(cancelFeedbackStatus)) {
                    entity.setCancelAgreedQty(getNewValue(entity.getCancelAgreedQty()));
                }
                // 未签数量
                else if (!TaskUserCancelAssignStatusEnum.BPF.getCode().equals(cancelAssignStatus) && !TaskUserCancelAssignStatusEnum.WPF.getCode().equals(cancelAssignStatus) && TaskUserCancelFeedbackStatusEnum.WQ.getCode().equals(cancelFeedbackStatus)) {
                    entity.setCancelUnsignedQty(getNewValue(entity.getCancelUnsignedQty()));
                }
            }
        }
        // 工程队Ids
        if (!CollectionUtils.isEmpty(engineersTeamIdList)) {
            entity.setEngineersTeamIds(String.join(",", engineersTeamIdList));
        }
    }

    /**
     * 计算相关的数量
     *
     * @param entity 停电任务对象
     */
    public static void calQty(Task entity) {
        calQty(entity, false);
    }

    /**
     * 获取当前用户所属工程队
     */
    public static Serializable getEngineersTeamId() {
        // 获取当前用户所属项目组ID
        Map<String, Object> extendData = TecpieSecurityUtil.getUser().getExtendData();
        return ObjectUtils.isEmpty(extendData.get("engineersTeamId")) ? null : (Serializable) extendData.get("engineersTeamId");
    }

    /**
     * 获取当前用户所属工程队
     */
    public static String getEngineersTeamIdStr() {
        Serializable engineersTeamId = getEngineersTeamId();
        return ObjectUtils.isEmpty(engineersTeamId) ? null : engineersTeamId.toString();
    }

    /**
     * 获取执行状态
     */
    public static String getTaskExecuteStatus(Task task) {
        // 当前执行状态
        TaskExecuteStatus taskExecuteStatus = task.getTaskExecuteStatus();
        if (ObjectUtils.isEmpty(taskExecuteStatus)) {
            throw BusinessException.build(SystemError.NO_SUCH_OBJECT_ERROR, String.format("停电任务ID[%s]执行状态不存在", task.getId()));
        }
        return taskExecuteStatus.getExecuteStatus();
    }

    /**
     * 判断状态是否已取消、已完成
     */
    public static boolean executeStatusValid(String taskExecuteStatus) {
        return executeStatusYqx(taskExecuteStatus) || executeStatusYwc(taskExecuteStatus);
    }

    /**
     * 判断状态是否待提交
     */
    public static boolean executeStatusDtj(String s) {
        return TaskExecuteStatusEnum.DTJ.getCode().equals(s);
    }

    /**
     * 判断状态是否待派发
     */
    public static boolean executeStatusDpf(String s) {
        return TaskExecuteStatusEnum.DPF.getCode().equals(s);
    }

    /**
     * 判断状态是否执行中
     */
    public static boolean executeStatusZxz(String s) {
        return TaskExecuteStatusEnum.ZXZ.getCode().equals(s);
    }

    /**
     * 判断状态是否已反馈
     */
    public static boolean executeStatusYfk(String s) {
        return TaskExecuteStatusEnum.YFK.getCode().equals(s);
    }

    /**
     * 判断状态是否已取消
     */
    public static boolean executeStatusYqx(String s) {
        return TaskExecuteStatusEnum.YQX.getCode().equals(s);
    }

    /**
     * 判断状态是否已完成
     */
    public static boolean executeStatusYwc(String s) {
        return TaskExecuteStatusEnum.YWC.getCode().equals(s);
    }

    /**
     * 在原值的基础上+1，返回新值
     */
    private static Integer getNewValue(Integer value) {
        if (ObjectUtils.isEmpty(value)) {
            value = 0;
        }
        return value + 1;
    }

    /**
     * 将Serializable转换成List<Serializable>
     */
    public static List<Serializable> toList(Serializable id) {
        List<Serializable> list = Lists.newArrayList();
        if (!ObjectUtils.isEmpty(id)) {
            list.add(id);
        }
        return list;
    }

    /**
     * 将ids转换成List<Serializable>
     */
    public static List<Serializable> toList(String ids) {
        Long[] idArr = Convert.toLongArray(ids);
        List<Serializable> list = Lists.newArrayList();
        Collections.addAll(list, idArr);
        return list;
    }

    /**
     * 获取新的版本号
     */
    public static Long getNewVersion(Long version) {
        if (ObjectUtils.isEmpty(version)) {
            version = TaskConstants.DEFAULT_VERSION;
        }
        return version + 1;
    }

    /**
     * 转换Object值为String
     */
    public static String convertToString(Object value) {
        if (ObjectUtils.isEmpty(value)) {
            return "";
        }
        return value.toString();
    }

    /**
     * 转换Object值为String
     */
    public static String convertToString(List<Object> list, int index) {
        if (CollectionUtils.isEmpty(list) || list.size() <= index) {
            return "";
        }
        Object value = list.get(index);
        return convertToString(value);
    }

    /**
     * 转换Object值为Integer
     */
    public static Integer convertToInteger(Object value) {
        if (ObjectUtils.isEmpty(value)) {
            return null;
        }
        return Integer.parseInt(value.toString());
    }

    /**
     * 转换Object值为LocalDate
     */
    public static LocalDate convertToLocalDate(Object value) {
        if (ObjectUtils.isEmpty(value)) {
            return null;
        }
        LocalDate localdate;
        try {
            localdate = DateUtil.parse(TaskUtil.convertToString(value)).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return localdate;
    }

    /**
     * 获取字典Map,以name为Key
     */
    public static Map<String, String> getLovLileNameMap(List<SysLov> lovList) {
        Map<String, String> resultMap = Maps.newHashMap();
        // 获取行
        List<SysLovLine> lovLineList = getLovLineList(lovList);
        for (SysLovLine item : lovLineList) {
            resultMap.put(item.getName(), item.getCode());
        }
        return resultMap;
    }

    private static List<SysLovLine> getLovLineList(List<SysLov> lovList) {
        if (CollectionUtils.isEmpty(lovList)) {
            return Lists.newArrayList();
        }
        SysLov sysLov = lovList.get(0);
        List<SysLovLine> lovLineList = sysLov.getLovLineList();
        if (CollectionUtils.isEmpty(lovLineList)) {
            return Lists.newArrayList();
        }
        return lovLineList;
    }

}
