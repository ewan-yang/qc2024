package com.tecpie.platform.task_repeat.service;

import com.tecpie.library.common.exception.BusinessException;
import com.tecpie.library.common.exception.SystemError;
import com.tecpie.platform.common.feign.advanceRule.AdvanceRuleFeignClient;
import com.tecpie.platform.task.entity.Task;
import com.tecpie.platform.task.service.TaskService;
import com.tecpie.platform.task_repeat.api.resource.TaskRepeatResource;
import com.tecpie.platform.task_user.entity.TaskUser;
import com.tecpie.platform.task_user.service.TaskUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class TaskRepeatService {

    private final TaskService taskService;

    private final TaskUserService taskUserService;

    private final AdvanceRuleFeignClient advanceRuleFeignClient;

    @Lazy
    public TaskRepeatService(TaskService taskService, TaskUserService taskUserService, AdvanceRuleFeignClient advanceRuleFeignClient) {
        this.taskService = taskService;
        this.taskUserService = taskUserService;
        this.advanceRuleFeignClient = advanceRuleFeignClient;
    }

    public List<TaskRepeatResource> searchTaskRepeatAll() {
        // 查询目标数据 任务和用户数据
        List<Task> sourceTaskList = taskService.lambdaQuery().eq(Task::getDeleted, 0).eq(Task::getIsRepeat, 0).list();
        if (sourceTaskList == null || sourceTaskList.isEmpty()) {
            return Collections.emptyList();
        } else {
            List<Serializable> sourceTaskIdList = sourceTaskList.stream().map(Task::getId).collect(Collectors.toList());
            List<TaskUser> sourceList = taskUserService.list().stream().filter(e -> sourceTaskIdList.contains(e.getTaskId())
                    && e.getUserType() != null && e.getUserType() != 7 && e.getDeleted() == 0).collect(Collectors.toList());

            Map<Serializable, Task> sourceTaskMap = this.getMapByTask(sourceTaskList);
            Map<Serializable, Task> sourceMap = this.getMapByTaskUser(sourceList, sourceTaskMap);

            // 根据目标数据的户号 查询比较数据 任务和用户数据
            List<String> accountNumberList = sourceList.stream().map(TaskUser::getAccountNumber).collect(Collectors.toList());
            List<TaskUser> compareList = taskUserService.list().stream().filter(e -> accountNumberList.contains(e.getAccountNumber())
                    && e.getUserType() != null && e.getUserType() != 7 && e.getDeleted() == 0).collect(Collectors.toList());
            List<Serializable> compareTaskIdList = compareList.stream().map(TaskUser::getTaskId).collect(Collectors.toList());
            List<Task> compareTaskList = taskService.list().stream().filter(e -> compareTaskIdList.contains(e.getId()) && e.getDeleted() == 0).collect(Collectors.toList());

            Map<Serializable, Task> compareTaskMap = this.getMapByTask(compareTaskList);
            Map<Serializable, Task> compareMap = this.getMapByTaskUser(compareList, compareTaskMap);

            // 遍历目标和比较数据 进行记录汇总
            return this.countRepeatResult(sourceList, sourceMap, compareList, compareMap, null, null);
        }
    }

    private Map<Serializable, Task> getMapByTask(List<Task> taskList) {
        Map<Serializable, Task> taskMap = new LinkedHashMap<>();
        for (Task task : taskList) {
            if (!taskMap.containsKey(task.getId())) {
                taskMap.put(task.getId(), task);
            }
        }
        return taskMap;
    }

    private Map<Serializable, Task> getMapByTaskUser(List<TaskUser> taskUserList, Map<Serializable, Task> sourceTaskMap) {
        Map<Serializable, Task> taskUserMap = new LinkedHashMap<>();
        for (TaskUser taskUser : taskUserList) {
            if (sourceTaskMap.containsKey(taskUser.getTaskId())) {
                taskUserMap.put(taskUser.getId(), sourceTaskMap.get(taskUser.getTaskId()));
            } else {
                taskUserMap.put(taskUser.getId(), new Task());
            }
        }
        return taskUserMap;
    }

    public List<TaskRepeatResource> searchTaskRepeatSingle(String taskCode) {
        // 查询属于当前任务单及其用户
        Task sourceTask = taskService.lambdaQuery().eq(Task::getTaskCode, taskCode).one();
        if (sourceTask.getIsRepeat() == 2) {
            return Collections.emptyList();
        } else {
            List<TaskUser> sourceList = taskUserService.list().stream().filter(e -> Objects.equals(e.getTaskId(), sourceTask.getId())
                    && e.getUserType() != null && e.getUserType() != 7 && e.getDeleted() == 0).collect(Collectors.toList());
            // 根据目标数据的户号 查询比较数据 任务和用户数据
            List<String> accountNumberList = sourceList.stream().map(TaskUser::getAccountNumber).collect(Collectors.toList());
            List<TaskUser> compareList = taskUserService.list().stream().filter(e -> accountNumberList.contains(e.getAccountNumber())
                    && e.getUserType() != null && e.getUserType() != 7 && e.getDeleted() == 0).collect(Collectors.toList());

            // 遍历目标和比较数据 进行记录汇总
            return this.countRepeatResult(sourceList, null, compareList, null, sourceTask, "");
        }
    }

    private List<TaskRepeatResource> countRepeatResult(List<TaskUser> sourceList,  Map<Serializable, Task> sourceMap,
                                                       List<TaskUser> compareList, Map<Serializable, Task> compareMap,
                                                       Task sourceTask, String type) {
        // 获取重复停电预警规则
        Map<String, Integer> voltageLevelMap = advanceRuleFeignClient.getRepeatPowerCutRule().getData();
        if (ObjectUtils.isEmpty(voltageLevelMap)) {
            throw BusinessException.build(SystemError.THIRD_INTERFACE_ERROR, "未查询到重复停电预警规则数据，请检查");
        }

        List<TaskRepeatResource> repeatResourceList = new ArrayList<>();
        for (TaskUser source : sourceList) {
            for (TaskUser compare : compareList) {
                // 初始化
                TaskRepeatResource repeatResource = new TaskRepeatResource();
                List<String> taskCodeList = new ArrayList<>();
                List<String> accessNumberList = new ArrayList<>();
                LocalDate sourceDate;
                LocalDate compareDate;
                boolean needAddCondition;
                // 单条查询会用到
                Task compareTask = taskService.lambdaQuery().eq(Task::getId, compare.getTaskId()).eq(Task::getDeleted, 0).one();

                // 以下任意符合一类场景 则过滤 ①户号不一样 ②电压等级不一样 ③停电时间为空
                if (type != null) {
                    if (!Objects.equals(source.getAccountNumber(), compare.getAccountNumber()) || !Objects.equals(source.getVoltageLevel(), compare.getVoltageLevel())
                            || compareTask == null || compareTask.getStartTime() == null || sourceTask.getStartTime() == null) {
                        continue;
                    }
                    needAddCondition = !Objects.equals(compare.getTaskId(), sourceTask.getId());
                    sourceDate = sourceTask.getStartTime().toLocalDate();
                    compareDate = compareTask.getStartTime().toLocalDate();
                } else {
                    LocalDateTime compareTime = compareMap.get(compare.getId()).getStartTime();
                    LocalDateTime sourceTime = sourceMap.get(source.getId()).getStartTime();
                    // 户号或者电压等级不匹配或者停电时间为空 则过滤
                    if (!Objects.equals(source.getAccountNumber(), compare.getAccountNumber()) || !Objects.equals(source.getVoltageLevel(), compare.getVoltageLevel())
                            || compareTime == null || sourceTime == null) {
                        continue;
                    }
                    needAddCondition = !Objects.equals(compare.getTaskId(), source.getTaskId());
                    sourceDate = sourceTime.toLocalDate();
                    compareDate = compareTime.toLocalDate();
                }

                int daysNum = (int) (sourceDate.toEpochDay() - compareDate.toEpochDay());

                // 获取重复停电阈值
                Integer days = voltageLevelMap.get(compare.getVoltageLevel());
                if (days == null) {
                    throw BusinessException.build(SystemError.NO_SUCH_OBJECT_ERROR, String.format("电压等级[%s]预警规则未维护，请检查", compare.getVoltageLevel()));
                }

                // 天数小于阈值 过滤重复任务单编号
                if (daysNum >= 0 && daysNum <= days && needAddCondition) {
                    // 任务单编号
                    taskCodeList.add(type != null ? sourceTask.getTaskCode() : sourceMap.get(source.getId()).getTaskCode());
                    taskCodeList.add(type != null ? compareTask.getTaskCode() : compareMap.get(compare.getId()).getTaskCode());
                    // 户号
                    accessNumberList.add(source.getAccountNumber());
                    // 台区
                    String region = source.getRegion();

                    repeatResource.setTaskCodeList(taskCodeList);
                    repeatResource.setAccessNumberList(accessNumberList);
                    repeatResource.setRegion(region);

                    boolean isAdd = true;
                    for (TaskRepeatResource resource : repeatResourceList) {
                        if (taskCodeList.size() == resource.getTaskCodeList().size() && new HashSet<>(taskCodeList).containsAll(resource.getTaskCodeList())
                                && resource.getRegion().equals(region)) {
                            for (String accessNumber : repeatResource.getAccessNumberList()) {
                                if (!resource.getAccessNumberList().contains(accessNumber)) {
                                    resource.getAccessNumberList().add(accessNumber);
                                }
                            }
                            isAdd = false;
                            break;
                        }
                    }
                    if (isAdd) {
                        repeatResourceList.add(repeatResource);
                    }
                }
            }
        }
        return repeatResourceList;
    }

    /**
     * 确认看完后将所有数据变更为1 单条数据不再显示变更为2
     */
    public void updateTaskRepeatAllOrSingle(String taskCode) {
        if (taskCode != null && !taskCode.isEmpty()) {
            taskService.lambdaUpdate().eq(Task::getTaskCode, taskCode).set(Task::getIsRepeat, 2).update();
        } else {
            taskService.lambdaUpdate().eq(Task::getIsRepeat, 0).set(Task::getIsRepeat, 1).update();
        }
    }
}
