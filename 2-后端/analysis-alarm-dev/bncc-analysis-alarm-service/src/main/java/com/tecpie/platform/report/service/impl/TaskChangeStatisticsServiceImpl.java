package com.tecpie.platform.report.service.impl;

import com.google.common.collect.Maps;
import com.tecpie.platform.common.feign.user.SysUserFeignClient;
import com.tecpie.platform.common.util.ExcelTransfer;
import com.tecpie.platform.report.api.command.query.TaskChangePageQueryCommand;
import com.tecpie.platform.report.api.command.query.TaskChangeQueryCommand;
import com.tecpie.platform.report.api.resource.TaskChangeStatisticsResource;
import com.tecpie.platform.report.api.resource.TaskChangeTotalResource;
import com.tecpie.platform.report.controller.assembler.resource.TaskChangeStatisticsResourceAssembler;
import com.tecpie.platform.report.entity.TaskChangeStatistics;
import com.tecpie.platform.report.mapper.TaskChangeStatisticsMapper;
import com.tecpie.platform.report.service.TaskChangeStatisticsService;
import com.tecpie.platform.user.business.organization.api.command.query.SysUserQueryCommand;
import com.tecpie.platform.user.business.organization.api.resource.SysUserResource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 通知变更数据统计 服务类实现
 *
 * @author zhijie.tan
 * @since 2023-08-01
 */
@Slf4j
@Service
public class TaskChangeStatisticsServiceImpl implements TaskChangeStatisticsService {

    private TaskChangeStatisticsMapper mapper;
    private SysUserFeignClient sysUserFeignClient;
    private ExcelTransfer excelTransfer;

    @Autowired
    public void setTaskChangeStatisticsServiceImpl(TaskChangeStatisticsMapper mapper, SysUserFeignClient sysUserFeignClient,
                                                   ExcelTransfer excelTransfer) {
        this.mapper = mapper;
        this.sysUserFeignClient = sysUserFeignClient;
        this.excelTransfer = excelTransfer;
    }

    @Override
    public List<TaskChangeStatistics> searchExtensionPageList(TaskChangeQueryCommand command) {
        command.initDateParam();
        // 查询版本大于1的数据
        command.setVersionGtn(1L);
        return this.mapper.searchExtensionPageList(command);
    }

    @Override
    public TaskChangeTotalResource getTaskChangeTotal(TaskChangeQueryCommand command) {
        // 查询统计数据
        command.initDateParam();
        List<TaskChangeStatistics> taskChangeList = this.mapper.searchExtensionPageList(command);
        // 获取当前登录用户信息
        Map<String, String> userMap = Maps.newHashMap();
        if (!CollectionUtils.isEmpty(taskChangeList)) {
            List<Long> userIdList = taskChangeList.stream().map(TaskChangeStatistics::getTaskSource).filter(ObjectUtils::isNotEmpty).collect(Collectors.toList());
            SysUserQueryCommand userQueryCommand = new SysUserQueryCommand();
            userQueryCommand.setIds(userIdList);
            List<SysUserResource> sysUserResourceList = sysUserFeignClient.getUserListByIds(userQueryCommand).getData();
            if (!CollectionUtils.isEmpty(sysUserResourceList)) {
                userMap = sysUserResourceList.stream().collect(Collectors.toMap(e -> e.getId().toString(), SysUserResource::getName));
            }
        }
        // 去年同期  确认时间减1年
        command.setConfirmTimeBegin(command.getConfirmTimeBegin().plusYears(-1));
        command.setConfirmTimeEnd(command.getConfirmTimeEnd().plusYears(-1));
        List<TaskChangeStatistics> preYearDataList = this.mapper.searchExtensionPageList(command);
        // 统计信息对象
        TaskChangeTotalResource totalResource = new TaskChangeTotalResource();
        totalResource.initData(TaskChangeStatisticsResourceAssembler.INSTANCE.parseList(taskChangeList), TaskChangeStatisticsResourceAssembler.INSTANCE.parseList(preYearDataList), userMap);
        return totalResource;
    }

    @Override
    public void taskChangeStatisticsExport(TaskChangePageQueryCommand command, HttpServletResponse response) {
        List<TaskChangeStatistics> entityList = this.searchExtensionPageList(command.getCondition());
        List<TaskChangeStatisticsResource> list = TaskChangeStatisticsResourceAssembler.INSTANCE.parseList(entityList);
        try {
            excelTransfer.exportExcel(response, list, "通知变更统计列表", "sheet1", TaskChangeStatisticsResource.class);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

}